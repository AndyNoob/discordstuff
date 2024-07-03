package me.comfortable_andy.discordstuff.markdown;

import lombok.Data;
import me.comfortable_andy.discordstuff.Main;
import me.comfortable_andy.discordstuff.markdown.parser.MarkdownParser;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
            return Optional.ofNullable(MarkdownParser.Type.find(Main.getInstance().getConfig().getString("parser", "fancy").toUpperCase())).orElse(MarkdownParser.Type.OFF);
        } catch (IllegalArgumentException e) {
            return MarkdownParser.Type.FANCY;
        }
    }

    public static Markdown[] getOrderedMarkdowns() {
        return Markdown.MARKDOWNS.values().stream()
                .sorted().toArray(Markdown[]::new);
    }

}
