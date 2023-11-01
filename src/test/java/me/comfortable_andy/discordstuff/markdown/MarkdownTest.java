package me.comfortable_andy.discordstuff.markdown;

import me.comfortable_andy.discordstuff.markdown.markdowns.MarkdownBold;
import me.comfortable_andy.discordstuff.markdown.markdowns.MarkdownItalic;
import me.comfortable_andy.discordstuff.markdown.markdowns.MarkdownStrikethrough;
import me.comfortable_andy.discordstuff.markdown.markdowns.MarkdownUnderline;
import me.comfortable_andy.discordstuff.markdown.parser.FancyParser;
import org.bukkit.ChatColor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MarkdownTest {

    @BeforeAll
    public static void setUp() {
        new MarkdownBold();
        new MarkdownItalic();
        new MarkdownStrikethrough();
        new MarkdownUnderline();
    }

    @Test
    public void check() {
        String input = "This is a **te~~st** of~~ *markdown* and ~~it's~~ __working__!";
        String expected = "This is a " + ChatColor.BOLD + "te" + ChatColor.STRIKETHROUGH + "st" + ChatColor.RESET + ChatColor.STRIKETHROUGH + " of" + ChatColor.RESET + " " + ChatColor.ITALIC + "markdown" + ChatColor.RESET + " and " + ChatColor.STRIKETHROUGH + "it's" + ChatColor.RESET + " " + ChatColor.UNDERLINE + "working" + ChatColor.RESET + "!";

        assertEquals(expected, new FancyParser().parse(input));

        input = "1 * 2 * 3 = 6";
        expected = input;

        assertEquals(expected, new FancyParser().parse(input));
        /*assertEquals(expected, new DiscordParser().parse(expected));*/

        input = "**AHHH**";
        expected = ChatColor.BOLD + "AHHH" + ChatColor.RESET;

        assertEquals(expected, new FancyParser().parse(input));
    }
}