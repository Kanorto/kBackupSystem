package vv0ta3fa9.plugin.kBackupSystem.utils.Color.impl;
import vv0ta3fa9.plugin.kBackupSystem.utils.Color.Colorizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LegacyColorizer extends Utils implements Colorizer {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([a-fA-F\\d]{6})");

    @Override
    public String colorize(String message) {
        if (message == null || message.isEmpty()) {
            return message;
        }
        final Matcher matcher = HEX_PATTERN.matcher(message);
        final StringBuilder builder = new StringBuilder(message.length() + 32);
        while (matcher.find()) {
            char[] group = matcher.group(1).toCharArray();
            matcher.appendReplacement(builder,
                    COLOR_CHAR + "x" +
                            COLOR_CHAR + group[0] +
                            COLOR_CHAR + group[1] +
                            COLOR_CHAR + group[2] +
                            COLOR_CHAR + group[3] +
                            COLOR_CHAR + group[4] +
                            COLOR_CHAR + group[5]);
        }
        message = matcher.appendTail(builder).toString();
        return translateAlternateColorCodes('&', message);
    }

}
