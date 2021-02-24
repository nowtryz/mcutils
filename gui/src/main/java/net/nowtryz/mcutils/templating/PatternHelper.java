package net.nowtryz.mcutils.templating;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.mu.util.stream.BiStream;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

// TODO show error if a char in the pattern is not present in the keys

public class PatternHelper {
    public static Pattern compile(File file) {
        return compile( YamlConfiguration.loadConfiguration(file));
    }

    public static Pattern compile(Configuration configuration) {
        List<String> patternList = configuration.getStringList("pattern");

        if (patternList.size() == 0 || patternList.size() > 6) {
            throw new IllegalArgumentException("Pattern must be a string list of at max 6 lines, got " + patternList.size());
        }

        List<String> patternChars = patternList.stream()
                .map(String::trim)
                .map(s -> s.split("\\s+"))
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());

        if ((patternChars.size() % 9) != 0) {
            throw new IllegalArgumentException("Pattern must be a list of 9 keysSection");
        }

        ConfigurationSection keysSection =  configuration.getConfigurationSection("keys");
        if (keysSection == null) throw new IllegalArgumentException("Pattern must contain a keys section");
        Map<String, PatternKey> keys = keysSection.getKeys(false)
                .stream()
                .map(keysSection::getConfigurationSection)
                .map(keySection -> PatternKeyFactory.fromSection(keySection, patternChars))
                .collect(Collectors.toMap(PatternKey::getKey, Function.identity()));

        List<PatternKey> pattern = patternChars.stream()
                .map(keys::get)
                .collect(Collectors.toList());

        ConfigurationSection hooksSection = configuration.getConfigurationSection("hooks");
        if (hooksSection == null) throw new IllegalArgumentException("Pattern must contain a hooks section");
        Map<String, PatternKey> hooks = hooksSection.getKeys(false)
                .stream()
                .collect(BiStream.toBiStream())
                .mapValues((Function<String, String>) hooksSection::getString)
                .mapValues(keys::get)
                .toMap();

        return new Pattern(ImmutableList.copyOf(pattern), ImmutableMap.copyOf(keys), hooks);
    }
}
