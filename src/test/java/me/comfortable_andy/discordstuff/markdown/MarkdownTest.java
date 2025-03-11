package me.comfortable_andy.discordstuff.markdown;

import me.comfortable_andy.discordstuff.markdown.markdowns.MarkdownBold;
import me.comfortable_andy.discordstuff.markdown.markdowns.MarkdownItalic;
import me.comfortable_andy.discordstuff.markdown.markdowns.MarkdownStrikethrough;
import me.comfortable_andy.discordstuff.markdown.markdowns.MarkdownUnderline;
import me.comfortable_andy.discordstuff.markdown.parser.DiscordParser;
import me.comfortable_andy.discordstuff.markdown.parser.FancyParser;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("deprecation")
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
        Map<String, String> inputExpected = new HashMap<>() {{
            put("This is a **te~~st** of~~ *markdown* and ~~it's~~ __working__!", "This is a " + ChatColor.BOLD + "te" + ChatColor.STRIKETHROUGH + "st" + ChatColor.RESET + ChatColor.STRIKETHROUGH + " of" + ChatColor.RESET + " " + ChatColor.ITALIC + "markdown" + ChatColor.RESET + " and " + ChatColor.STRIKETHROUGH + "it's" + ChatColor.RESET + " " + ChatColor.UNDERLINE + "working" + ChatColor.RESET + "!");
            put("1 * 2 * 3 = 6", "1 * 2 * 3 = 6");
            put("**AHHH**", ChatColor.BOLD + "AHHH" + ChatColor.RESET);
            put("__", "__");
            put("~~", "~~");
            put("**", "**");
            put("*something", "*something");
            put("__Re__**loaded**!", ChatColor.UNDERLINE + "Re" + ChatColor.RESET + ChatColor.BOLD + "loaded" + ChatColor.RESET + "!");
            put("****", ChatColor.BOLD + "" + ChatColor.RESET);
            put("***", "***");
            put("**sup " + ChatColor.RED + "you suck** boi", ChatColor.BOLD + "sup " + ChatColor.RED + "you suck" + ChatColor.RESET + ChatColor.RED + " boi");
            put("_**hi Comfortable_Andy hi**_", ChatColor.ITALIC + "" + ChatColor.BOLD + "hi Comfortable_Andy hi" + ChatColor.RESET + "" + ChatColor.ITALIC + "" + ChatColor.RESET);
            put("**_hi Comfortable_Andy hi_**", ChatColor.BOLD + "" + ChatColor.ITALIC + "hi Comfortable_Andy hi" + ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.RESET);
        }};
        for (Map.Entry<String, String> entry : inputExpected.entrySet()) {
            assertEquals(entry.getValue(), new FancyParser().parse(entry.getKey()));
        }
    }

    @Test
    public void testDiscord() {
        Map<String, String> inputExpected = new HashMap<>(){{
            put("1 * 2 * 3 = 6", "1 * 2 * 3 = 6");
            put("**AHHH**", ChatColor.BOLD + "AHHH" + ChatColor.RESET);
            put("This is a **te~~st** of ~~ *markdown* and ~~ it's~~ __working__!", "This is a " + ChatColor.BOLD + "te~~st" + ChatColor.RESET + " of " + ChatColor.STRIKETHROUGH + " " + ChatColor.ITALIC + "markdown" + ChatColor.RESET + ChatColor.STRIKETHROUGH + " and " + ChatColor.RESET + " it's~~ " + ChatColor.UNDERLINE + "working" + ChatColor.RESET + "!");
            put("**sup " + ChatColor.RED + "you suck** boi", ChatColor.BOLD + "sup " + ChatColor.RED + "you suck" + ChatColor.RESET + ChatColor.RED + " boi");
            put("**_hi Comfortable_Andy hi_**", ChatColor.BOLD + "" + ChatColor.ITALIC + "hi Comfortable_Andy hi" + ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.RESET);
            put("_hi_!", s("<i>hi</i>!"));
            put("***hi***!", ("" + ChatColor.BOLD + ChatColor.ITALIC + "hi" + ChatColor.RESET + ChatColor.BOLD + ChatColor.RESET + "!"));
        }};
        for (Map.Entry<String, String> entry : inputExpected.entrySet()) {
            assertEquals(entry.getValue(), new DiscordParser().parse(entry.getKey()));
        }
    }

    private String s(String i) {
        return (LegacyComponentSerializer.legacySection().serialize(MiniMessage.miniMessage().deserialize(i)));
    }

}