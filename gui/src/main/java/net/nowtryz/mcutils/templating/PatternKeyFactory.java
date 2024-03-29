package net.nowtryz.mcutils.templating;

import com.google.common.collect.Sets;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.nowtryz.mcutils.MCUtils;
import net.nowtryz.mcutils.builder.ItemBuilders;
import net.nowtryz.mcutils.builder.api.ItemBuilder;
import net.nowtryz.mcutils.builder.api.SkullBuilder;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
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
    private final List<String> patternChars;

    public PatternKey fromSection(ConfigurationSection section) {
        String key = section.getName();
        ItemStack item = parseItem(section);
        ItemStack fallStack = section.contains("fallback") ?
                parseItem(section.getConfigurationSection("fallback")) : null;
        int[] positions = this.getPositionForCharacter(key);

        return new PatternKey(key, positions, item, fallStack);
    }

    public PatternKey withGivenItemStack(String key, ItemStack item) {
        int[] positions = this.getPositionForCharacter(key);
        return new PatternKey(key, positions, item, null);
    }

    public PatternKey fromKey(String key) {
        return this.withGivenItemStack(key, new ItemStack(Material.AIR));
    }

    private int[] getPositionForCharacter(String key) {
        return IntStream.range(0, patternChars.size())
                .filter(i -> key.equals(patternChars.get(i))) // Only keep those indices
                .toArray();
    }

    private static ItemStack parseItem(ConfigurationSection section) {
        ItemBuilder builder = extractBaseItem(section);

        if (section.contains("data")) builder.setDurability((short) section.getInt("data", 0));
        if (section.contains("potion")) setPotionType(builder, section);
        if (section.contains("color")) builder.setColor(parseColor(section.getString("color")));
        if (section.contains("dye")) builder.setDyeColor(parseColor(section.getString("dye")));
        if (section.contains("name")) builder.setDisplayName(parseAmp(section.getString("name")));
        if (section.contains("glowing")) builder.setGlowing(section.getBoolean("glowing"));
        if (!section.getBoolean("attributes", true)) builder.addAllItemFlags();
        if (section.contains("lore")) builder.setLore(section.getStringList("lore")
                .stream()
                .map(PatternKeyFactory::parseAmp)
                .collect(Collectors.toList()));

        return builder.build();
    }

    private static void setPotionType(ItemBuilder builder, ConfigurationSection section) {
        String value = section.getString("potion");
        if (value == null) return;

        try {
            PotionType type = PotionType.valueOf(formatEnumName(value));
            builder.setPotionType(type);
        } catch (IllegalArgumentException ignored) {
            Bukkit.getLogger().warning(String.format("[Templating] %s.potion (%s) is not a valid potion type", section.getCurrentPath(), value));
            Bukkit.getLogger().warning("[Templating] Valide potions are: " + Arrays.stream(PotionType.values())
                    .map(Enum::name)
                    .map(String::toLowerCase)
                    .collect(Collectors.joining(", ")));
        }
    }

    private static ItemBuilder extractBaseItem(ConfigurationSection section) {
        if (section.contains("skull")) return parseSkull(Objects.requireNonNull(section.getConfigurationSection("skull")));
        if (section.contains("material")) return parseMaterial(section);

        throw new IllegalArgumentException("Key must have one of the following properties: material, skull (" + section.getCurrentPath() + ")");
    }

    private static ItemBuilder parseMaterial(ConfigurationSection section) {
        String materialName = section.getString("material");

        if (materialName == null) throw new IllegalArgumentException(section.getCurrentPath() + ".material is missing");

        String formattedName = formatEnumName(materialName);
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

        return ItemBuilders.create(material);
    }

    private static SkullBuilder parseSkull(ConfigurationSection section) {
        SkullBuilder skull = ItemBuilders.createSkull();

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

    private static String formatEnumName(@NonNull String input) {
        String name = input
                    .replaceFirst("minecraft:", "")
                    .toUpperCase(Locale.ENGLISH)
                    .replaceAll("\\s+", "_")
                    .replaceAll("\\W", "");

        if (MCUtils.THIRTEEN_COMPATIBLE && colorables.contains(name)) return "WHITE_" + name;
        else return name;
    }
}
