package me.comfortable_andy.discordstuff.markdown;

import lombok.Data;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Data
public abstract class Markdown {

    public static final Map<String, Markdown> MARKDOWNS = new HashMap<>();

    protected final String name;
    protected final ChatColor color;
    protected final String[] characters;

    public Markdown(String name, ChatColor color, String... characters) {
        this.name = name;
        this.color = color;
        this.characters = characters;

        MARKDOWNS.put(name, this);
    }

    public final String check(String input) {
        String toReturn = input;

        for (String character : characters) {
            Pattern pattern =
                    Pattern.compile(character + "([^" + character + "]+)" + character);
            toReturn = pattern.matcher(toReturn).replaceAll(color + "$1" + ChatColor.RESET);
        }

        return toReturn;
    }
}
