package net.nowtryz.mcutils.templating;

import com.google.common.collect.Sets;
import lombok.NonNull;
import net.nowtryz.mcutils.MCUtils;
import net.nowtryz.mcutils.builder.ItemBuilder;
import net.nowtryz.mcutils.builder.SkullBuilder;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PatternKeyFactory {
    private final static Set<String> colorables = Sets.newHashSet(
        "BANNER",
        "BED",
        "CARPET",
        "CONCRETE",
        "CONCRETE_POWDER",
        "DYE",
        "GLAZED_TERRACOTTA",
        "SHULKER_BOX",
        "STAINED_GLASS",
        "STAINED_GLASS_PANE",
        "TERRACOTTA",
        "WALL_BANNER",
        "WOOL"
    );

    public static PatternKey fromSection(ConfigurationSection section, List<String> patternChars) {
        String key = section.getName();
        ItemStack item = parseItem(section);
        ItemStack fallStack = section.contains("fallback") ?
                parseItem(section.getConfigurationSection("fallback")) : null;
        int[] positions = IntStream.range(0, patternChars.size())
                .filter(i -> key.equals(patternChars.get(i))) // Only keep those indices
                .toArray();

        return new PatternKey(key, positions, item, fallStack);
    }

    private static ItemStack parseItem(ConfigurationSection section) {
        ItemBuilder<?> builder = extractBaseItem(section);

        if (section.contains("data")) builder.setDurability((short) section.getInt("data", 0));
        if (section.contains("color")) builder.setColor(parseColor(section.getString("color")));
        if (section.contains("dye")) builder.setDyeColor(parseColor(section.getString("dye")));
        if (section.contains("name")) builder.setDisplayName(parseAmp(section.getString("name")));
        if (section.contains("glowing")) builder.setGlowing(section.getBoolean("glowing"));
        if (section.contains("lore")) builder.setLore(section.getStringList("lore")
                .stream()
                .map(PatternKeyFactory::parseAmp)
                .collect(Collectors.toList()));

        return builder.build();
    }

    private static ItemBuilder<?> extractBaseItem(ConfigurationSection section) {
        if (section.contains("skull")) return parseSkull(Objects.requireNonNull(section.getConfigurationSection("skull")));
        if (section.contains("material")) return parseMaterial(section);

        throw new IllegalArgumentException("Key must have one of the following properties: material, skull (" + section.getCurrentPath() + ")");
    }

    private static ItemBuilder<?> parseMaterial(ConfigurationSection section) {
        String materialName = section.getString("material");

        if (materialName == null) throw new IllegalArgumentException(section.getCurrentPath() + ".material is missing");

        String formattedName = formatMaterial(materialName);
        Material material = Material.getMaterial(formattedName);

        if (material == null && MCUtils.THIRTEEN_COMPATIBLE) {
            material = Material.getMaterial(formattedName, true);
            if (material != null) Bukkit.getLogger().log(
                    Level.WARNING,
                    "[Templating] {0}.material ({1}) is a legacy material, consider changing to {2}",
                    new String[] {section.getCurrentPath(), formattedName, material.name()}
            );
        }

        if (material == null) {
            throw new IllegalArgumentException(section.getCurrentPath() + ".material is not a valid material");
        }

        return ItemBuilder.create(material);
    }

    private static SkullBuilder parseSkull(ConfigurationSection section) {
        SkullBuilder skull = ItemBuilder.createSkull();

        if (section.contains("data")) return skull.setValue(section.getString("data"));
        if (section.contains("url")) return skull.setTextureUrl(section.getString("url"));
        if (section.contains("player")) return skull.setName(section.getString("player"));

        throw new IllegalArgumentException("For a skull item, you must at least specify one of the following fields:\n" +
                "- data: data value of the head\n" +
                "- url: url of the head's texture\n" +
                "- player: the name of a player\n" +
                "(" + section.getCurrentPath() + ")"
        );
    }

    private static String parseAmp(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    private static DyeColor parseColor(String name) {
        Validate.notNull(name, "Name cannot be null");
        return DyeColor.valueOf(name
                .toUpperCase(Locale.ENGLISH)
                .replaceAll("\\s+", "_")
                .replaceAll("\\W", ""));
    }

    private static String formatMaterial(@NonNull String input) {
        String name = input
                    .replaceFirst("minecraft:", "")
                    .toUpperCase(Locale.ENGLISH)
                    .replaceAll("\\s+", "_")
                    .replaceAll("\\W", "");

        if (MCUtils.THIRTEEN_COMPATIBLE && colorables.contains(name)) return "WHITE_" + name;
        else return name;
    }
}
