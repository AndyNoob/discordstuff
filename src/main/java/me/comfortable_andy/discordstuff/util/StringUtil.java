package me.comfortable_andy.discordstuff.util;

public class StringUtil {

    public static boolean forwardMatches(String str, int index, boolean inclusive, String pattern) {
        if (!inclusive) index++;
        if (index < 0) throw new IndexOutOfBoundsException();
        if (index > str.length()) return false;
        final String substring = str.substring(index);
        return substring.matches("^" + pattern + ".*");
    }

    public static boolean backwardMatches(String str, int index, boolean inclusive, String pattern) {
        if (inclusive) index++;
        if (index < 0) throw new IndexOutOfBoundsException();
        if (index > str.length()) return false;
        return str.substring(0, index).matches(".*" + pattern + "$");
    }

    public static String regexEscape(String str) {
        return str.replaceAll("(.)", "\\\\$0");
    }

    public static String charAt(String str, int index) {
        return str.length() > index ? str.substring(index, index + 1) : "";
    }

}
