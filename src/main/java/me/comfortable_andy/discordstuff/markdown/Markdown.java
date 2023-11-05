package me.comfortable_andy.discordstuff.markdown;

import lombok.Data;
import me.comfortable_andy.discordstuff.Main;
import me.comfortable_andy.discordstuff.markdown.parser.FancyParser;
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
    protected boolean mustHaveSpaceAfter = false;

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
        return getParser().parse(input);
    }

    public static MarkdownParser getParser() {
        try {
            return Optional.ofNullable(MarkdownParser.Type.find(Main.getInstance().getConfig().getString("parser", "fancy").toUpperCase())).orElse(MarkdownParser.Type.OFF).getSupplier().get();
        } catch (IllegalArgumentException e) {
            return new FancyParser();
        }
    }

    public static Markdown[] getOrderedMarkdowns() {
        return Markdown.MARKDOWNS.values().stream()
                .sorted().toArray(Markdown[]::new);
    }

}
