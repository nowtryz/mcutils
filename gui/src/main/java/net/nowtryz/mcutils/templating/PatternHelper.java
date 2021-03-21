package net.nowtryz.mcutils.templating;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.mu.util.stream.BiStream;
import lombok.NonNull;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// TODO show error if a char in the pattern is not present in the keys

public class PatternHelper {
    private final static java.util.regex.Pattern KEY_EXTRACTOR = java.util.regex.Pattern.compile("^(.) (.) (.) (.) (.) (.) (.) (.) (.)$");
    private final static java.util.regex.Pattern SEPARATED_KEY_EXTRACTOR = java.util.regex.Pattern.compile("^(.)(.)(.)(.)(.)(.)(.)(.)(.)$");

    public static Pattern compile(File file) {
        return compile( YamlConfiguration.loadConfiguration(file));
    }

    public static Pattern compile(Configuration configuration) {
        List<String> patternList = configuration.getStringList("pattern");

        if (patternList.size() == 0 || patternList.size() > 6) {
            throw new IllegalArgumentException("Pattern must be a string list of at max 6 lines, got " + patternList.size());
        }

        List<String> patternChars = patternList.stream()
                .flatMap(PatternHelper::extractKeys)
                .collect(Collectors.toList());

        if ((patternChars.size() % 9) != 0) {
            throw new IllegalArgumentException("Pattern must be a list of 9 keysSection");
        }

        PatternKeyFactory factory = new PatternKeyFactory(patternChars);

        ConfigurationSection keysSection =  configuration.getConfigurationSection("keys");
        if (keysSection == null) throw new IllegalArgumentException("Pattern must contain a keys section");

        Map<String, PatternKey> keys = BiStream.biStream(patternChars)
                .distinct()
                .mapValues(keysSection::getConfigurationSection)
                .mapValues((s, section) -> section == null ? factory.fromKey(s) : factory.fromSection(section))
                .toMap();

        // ensure that space is air if present in the pattern
        if (patternChars.contains(" ") && !keys.containsKey(" ")) {
            keys.put(" ", factory.fromKey(" "));
        }

        List<PatternKey> pattern = patternChars.stream()
                // if a pattern key is missing in the keys, simply use air instead
                .map(keys::get)
                .collect(Collectors.toList());

        ConfigurationSection hooksSection = configuration.getConfigurationSection("hooks");
        if (hooksSection == null) throw new IllegalArgumentException("Pattern must contain a hooks section");
        Map<String, PatternKey> hooks = hooksSection.getKeys(true)
                .stream()
                .collect(BiStream.toBiStream())
                .mapValues((Function<String, String>) hooksSection::getString)
                .filterValues(keys::containsKey) // filter hooks not present on the pattern
                .mapValues(keys::get)
                .toMap();

        return new Pattern(ImmutableList.copyOf(pattern), ImmutableMap.copyOf(keys), hooks);
    }

    static Stream<String> extractKeys(@NonNull String line) {
        Matcher matcher = KEY_EXTRACTOR.matcher(line);

        if (!matcher.matches()) {
            matcher = SEPARATED_KEY_EXTRACTOR.matcher(line);
            Preconditions.checkArgument(matcher.matches(), "`%s` is not a valid line, it must be 9 keys", line);
        }

        return IntStream.rangeClosed(1,9).mapToObj(matcher::group);
    }
}
