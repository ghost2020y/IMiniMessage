package me.zortex.iminimessage.converter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LegacyConverter {

    private static final Pattern HEX_PATTERN_SHORT = Pattern.compile("[&§]#([a-fA-F0-9]{6})");
    private static final Pattern HEX_PATTERN_LONG = Pattern.compile("[&§]x([&§][a-fA-F0-9]){6}");

    public String convertToMiniMessage(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String result = input;

        Matcher hexMatcherShort = HEX_PATTERN_SHORT.matcher(result);
        StringBuilder sbShort = new StringBuilder();
        while (hexMatcherShort.find()) {
            hexMatcherShort.appendReplacement(sbShort, "<color:#" + hexMatcherShort.group(1) + ">");
        }
        hexMatcherShort.appendTail(sbShort);
        result = sbShort.toString();

        Matcher hexMatcherLong = HEX_PATTERN_LONG.matcher(result);
        StringBuilder sbLong = new StringBuilder();
        while (hexMatcherLong.find()) {
            String hex = hexMatcherLong.group()
                    .replaceAll("[&§]x", "")
                    .replaceAll("[&§]", "");
            hexMatcherLong.appendReplacement(sbLong, "<color:#" + hex + ">");
        }
        hexMatcherLong.appendTail(sbLong);
        result = sbLong.toString();

        result = result
                // Color
                .replace("&0", "<black>").replace("§0", "<black>")
                .replace("&1", "<dark_blue>").replace("§1", "<dark_blue>")
                .replace("&2", "<dark_green>").replace("§2", "<dark_green>")
                .replace("&3", "<dark_aqua>").replace("§3", "<dark_aqua>")
                .replace("&4", "<dark_red>").replace("§4", "<dark_red>")
                .replace("&5", "<dark_purple>").replace("§5", "<dark_purple>")
                .replace("&6", "<gold>").replace("§6", "<gold>")
                .replace("&7", "<gray>").replace("§7", "<gray>")
                .replace("&8", "<dark_gray>").replace("§8", "<dark_gray>")
                .replace("&9", "<blue>").replace("§9", "<blue>")
                .replace("&a", "<green>").replace("§a", "<green>")
                .replace("&b", "<aqua>").replace("§b", "<aqua>")
                .replace("&c", "<red>").replace("§c", "<red>")
                .replace("&d", "<light_purple>").replace("§d", "<light_purple>")
                .replace("&e", "<yellow>").replace("§e", "<yellow>")
                .replace("&f", "<white>").replace("§f", "<white>")
                // Formatting
                .replace("&k", "<obfuscated>").replace("§k", "<obfuscated>")
                .replace("&l", "<bold>").replace("§l", "<bold>")
                .replace("&m", "<strikethrough>").replace("§m", "<strikethrough>")
                .replace("&n", "<underlined>").replace("§n", "<underlined>")
                .replace("&o", "<italic>").replace("§o", "<italic>")
                .replace("&r", "<reset>").replace("§r", "<reset>");

        return result;
    }
}