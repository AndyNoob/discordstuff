package me.comfortable_andy.discordstuff.markdown.markdowns;

import me.comfortable_andy.discordstuff.markdown.Markdown;
import org.bukkit.ChatColor;

public class MarkdownStrikethrough extends Markdown {
    public MarkdownStrikethrough() {
        super("strikethrough", ChatColor.STRIKETHROUGH, 2, "~~");
    }
}
