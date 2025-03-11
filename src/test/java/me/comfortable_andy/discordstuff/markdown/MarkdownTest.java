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

import java.util.LinkedHashMap;
import java.util.Map;

import static org.bukkit.ChatColor.*;
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
        Map<String, String> inputExpected = new LinkedHashMap<>() {{
            put("This is a **te~~st** of~~ *markdown* and ~~it's~~ __working__!", "This is a " + BOLD + "te" + ChatColor.STRIKETHROUGH + "st" + RESET + ChatColor.STRIKETHROUGH + " of" + RESET + " " + ITALIC + "markdown" + RESET + " and " + ChatColor.STRIKETHROUGH + "it's" + RESET + " " + ChatColor.UNDERLINE + "working" + RESET + "!");
            put("1 * 2 * 3 = 6", "1 * 2 * 3 = 6");
            put("**AHHH**", BOLD + "AHHH" + RESET);
            put("__", "__");
            put("~~", "~~");
            put("**", "**");
            put("*something", "*something");
            put("__Re__**loaded**!", ChatColor.UNDERLINE + "Re" + RESET + BOLD + "loaded" + RESET + "!");
            put("****", BOLD + "" + RESET);
            put("***", "***");
            put("**sup " + ChatColor.RED + "you suck** boi", BOLD + "sup " + ChatColor.RED + "you suck" + RESET + ChatColor.RED + " boi");
            put("_**hi Comfortable_Andy hi**_", ITALIC + "" + BOLD + "hi Comfortable_Andy hi" + RESET + ITALIC + RESET);
            put("**_hi Comfortable_Andy hi_**", BOLD + "" + ITALIC + "hi Comfortable_Andy hi" + RESET + BOLD + RESET);
            put("***hi***!", ("" + BOLD + ITALIC + "hi" + RESET + BOLD + RESET + "!"));
        }};
        for (Map.Entry<String, String> entry : inputExpected.entrySet()) {
            assertEquals(entry.getValue(), new FancyParser().parse(entry.getKey()));
        }
    }

    @Test
    public void testDiscord() {
        Map<String, String> inputExpected = new LinkedHashMap<>(){{
            put("1 * 2 * 3 = 6", "1 * 2 * 3 = 6");
            put("**AHHH**", BOLD + "AHHH" + RESET);
            put("This is a **te~~st** of ~~ *markdown* and ~~ it's~~ __working__!", "This is a " + BOLD + "te~~st" + RESET + " of " + ChatColor.STRIKETHROUGH + " " + ITALIC + "markdown" + RESET + ChatColor.STRIKETHROUGH + " and " + RESET + " it's~~ " + ChatColor.UNDERLINE + "working" + RESET + "!");
            put("**sup " + ChatColor.RED + "you suck** boi", BOLD + "sup " + ChatColor.RED + "you suck" + RESET + ChatColor.RED + " boi");
            put("**_hi Comfortable_Andy hi_**", BOLD + "" + ITALIC + "hi Comfortable_Andy hi" + RESET + BOLD + RESET);
            put("_hi_!", s("<i>hi</i>!"));
            put("***hi***!", ("" + BOLD + ITALIC + "hi" + RESET + BOLD + RESET + "!"));
        }};
        for (Map.Entry<String, String> entry : inputExpected.entrySet()) {
            assertEquals(entry.getValue(), new DiscordParser().parse(entry.getKey()));
        }
    }

    @SuppressWarnings("SameParameterValue")
    private String s(String i) {
        return (LegacyComponentSerializer.legacySection().serialize(MiniMessage.miniMessage().deserialize(i)));
    }

}