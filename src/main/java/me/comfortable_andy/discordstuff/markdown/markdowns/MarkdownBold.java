package me.comfortable_andy.discordstuff.markdown.markdowns;

import me.comfortable_andy.discordstuff.markdown.Markdown;
import org.bukkit.ChatColor;

public class MarkdownBold extends Markdown {
    public MarkdownBold() {
        super("bold", ChatColor.BOLD, 0, "**");
    }
}
