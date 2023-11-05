package me.comfortable_andy.discordstuff.markdown;

import me.comfortable_andy.discordstuff.markdown.markdowns.MarkdownBold;
import me.comfortable_andy.discordstuff.markdown.markdowns.MarkdownItalic;
import me.comfortable_andy.discordstuff.markdown.markdowns.MarkdownStrikethrough;
import me.comfortable_andy.discordstuff.markdown.markdowns.MarkdownUnderline;
import me.comfortable_andy.discordstuff.markdown.parser.DiscordParser;
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
    public void testFancy() {
        String input = "This is a **te~~st** of~~ *markdown* and ~~it's~~ __working__!";
        String expected = "This is a " + ChatColor.BOLD + "te" + ChatColor.STRIKETHROUGH + "st" + ChatColor.RESET + ChatColor.STRIKETHROUGH + " of" + ChatColor.RESET + " " + ChatColor.ITALIC + "markdown" + ChatColor.RESET + " and " + ChatColor.STRIKETHROUGH + "it's" + ChatColor.RESET + " " + ChatColor.UNDERLINE + "working" + ChatColor.RESET + "!";

        assertEquals(expected, new FancyParser().parse(input));

        input = "1 * 2 * 3 = 6";
        expected = input;

        assertEquals(expected, new FancyParser().parse(input));

        input = "**AHHH**";
        expected = ChatColor.BOLD + "AHHH" + ChatColor.RESET;

        assertEquals(expected, new FancyParser().parse(input));

        assertEquals("__", new FancyParser().parse("__"));

        assertEquals("~~", new FancyParser().parse("~~"));

        assertEquals("**", new FancyParser().parse("**"));

        assertEquals("*something", new FancyParser().parse("*something"));

        input = "__Re__**loaded**!";
        expected = ChatColor.UNDERLINE + "Re" + ChatColor.RESET + ChatColor.BOLD + "loaded" + ChatColor.RESET + "!";

        assertEquals(expected, new FancyParser().parse(input));

        assertEquals(ChatColor.BOLD + "" + ChatColor.RESET, new FancyParser().parse("****"));

        assertEquals("***", new FancyParser().parse("***"));
    }

    @Test
    public void testDiscord() {
        String input = "1 * 2 * 3 = 6";
        String expected = input;

        assertEquals(expected, new DiscordParser().parse(expected));

        input = "**AHHH**";
        expected = ChatColor.BOLD + "AHHH" + ChatColor.RESET;

        assertEquals(expected, new DiscordParser().parse(input));

        input = "This is a **te~~st** of~~ *markdown* and ~~it's~~ __working__!";
        expected = "This is a " + ChatColor.BOLD + "te~~st" + ChatColor.RESET + " of" + ChatColor.STRIKETHROUGH + " " + ChatColor.ITALIC + "markdown" + ChatColor.RESET + ChatColor.STRIKETHROUGH + " and " + ChatColor.RESET + "it's~~ " + ChatColor.UNDERLINE + "working" + ChatColor.RESET + "!";

        assertEquals(expected, new DiscordParser().parse(input));
    }

}