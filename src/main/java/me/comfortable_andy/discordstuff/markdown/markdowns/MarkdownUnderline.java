package me.comfortable_andy.discordstuff.markdown.markdowns;

import me.comfortable_andy.discordstuff.markdown.Markdown;
import org.bukkit.ChatColor;

public class MarkdownUnderline extends Markdown {
    public MarkdownUnderline() {
        super("underline", ChatColor.UNDERLINE, "\\_\\_");
    }
}
