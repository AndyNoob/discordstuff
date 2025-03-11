package me.comfortable_andy.discordstuff.markdown;

import lombok.Data;
import me.comfortable_andy.discordstuff.DiscordStuffMain;
import me.comfortable_andy.discordstuff.markdown.parser.MarkdownParser;
import me.comfortable_andy.discordstuff.util.StringUtil;
import org.bukkit.ChatColor;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings({"deprecation", "BooleanMethodIsAlwaysInverted"})
@Data
public abstract class Markdown implements Comparable<Markdown> {

    protected static final Map<String, Markdown> MARKDOWNS = new HashMap<>();

    protected final String name;
    protected final ChatColor color;
    protected final String[] characters;
    protected final int orderIndex;
    protected boolean shouldTouchSpaceAndText = false;

    public Markdown(String name, ChatColor color, int orderIndex, String... characters) {
        this.name = name;
        this.color = color;
        this.orderIndex = orderIndex;
        this.characters = characters;

        MARKDOWNS.put(name, this);
    }

    public static String concatenate(String delimiter, Function<String, String> transformer) {
        return MARKDOWNS.values()
                .stream()
                .map(Markdown::getCharacters)
                .flatMap(Arrays::stream)
                .map(transformer)
                .collect(Collectors.joining(delimiter));
    }

    public static boolean spaceAndText(String input, int indexStart, int delta) {
        final String markdownPattern = concatenate("|", StringUtil::regexEscape);
        boolean markdownBefore = StringUtil.backwardMatches(input, indexStart, false, markdownPattern);
        final boolean markdownAfter = StringUtil.forwardMatches(input, indexStart + delta, true, markdownPattern);
        String after = StringUtil.charAt(input, indexStart + delta);
        String before = StringUtil.charAt(input, indexStart - 1);
        final boolean spaceBefore = before.isEmpty() || before.matches("\\W");
        final boolean spaceAfter = after.isEmpty() || after.matches("\\W");
        if (indexStart == 0) {
            return !spaceAfter || markdownAfter;
        } else if (indexStart == input.length() - 1) {
            return !spaceBefore || markdownBefore;
        } else if (spaceBefore) {
            return markdownAfter || !spaceAfter;
        } else if (spaceAfter) {
            return true;
        } else {
            return markdownAfter || markdownBefore;
        }
    }

    @Override
    public int compareTo(Markdown o) {
        return Integer.compare(orderIndex, o.orderIndex);
    }

    public static String convert(String input) {
        return convert(input, false);
    }

    public static String convert(String input, boolean keepTriggers) {
        return getParser().getSupplier().get().parse(input, keepTriggers);
    }

    public static MarkdownParser.Type getParser() {
        try {
            return Optional.ofNullable(MarkdownParser.Type.find(DiscordStuffMain.getInstance().getConfig().getString("parser", "fancy").toUpperCase())).orElse(MarkdownParser.Type.OFF);
        } catch (IllegalArgumentException e) {
            return MarkdownParser.Type.FANCY;
        }
    }

    public static Markdown[] getOrderedMarkdowns() {
        return Markdown.MARKDOWNS.values().stream()
                .sorted().toArray(Markdown[]::new);
    }

}
