package me.comfortable_andy.discordstuff.markdown.markdowns;

import me.comfortable_andy.discordstuff.markdown.Markdown;
import org.bukkit.ChatColor;

public class MarkdownItalic extends Markdown {
    public MarkdownItalic() {
        super("italic", ChatColor.ITALIC, 3, "*", "_");
    }
}
