package me.comfortable_andy.discordstuff.markdown.parser;

import me.comfortable_andy.discordstuff.markdown.Markdown;
import me.comfortable_andy.discordstuff.util.StringUtil;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static me.comfortable_andy.discordstuff.markdown.Markdown.concatenate;

/*
 * a.k.a. the discord way
 * keynotes for reverse engineering:
 * - there seems to be a special case for "*" that it will not be counted
 *   AS STARTING TRIGGER if it is followed by a space, everything else doesn't care
 * - when a markdown trigger is found, the next closest trigger of the same
 *   is searched for and the text in between is parsed recursively
 */
@SuppressWarnings("deprecation")
public class DiscordParser extends MarkdownParser {

    @Override
    public String parse(String input, boolean keepTriggers) {
        return this.parse0(input, keepTriggers, new ArrayList<>());
    }

    public String parse0(String input, boolean keep, List<ChatColor> currentColors) {
        if (input.isEmpty()) return input;
        final StringBuilder out = new StringBuilder();
        for (int i = 0; i < input.length(); ) {
            final String left = input.substring(i);
            Markdown markdown = null;
            String trigger = null;

            for (Markdown md : Markdown.getOrderedMarkdowns()) {
                if (markdown != null) break;
                for (String t : md.getCharacters()) {
                    if (!left.startsWith(t)) continue;
                    markdown = md;
                    trigger = t;
                    break;
                }
            }

            if (markdown == null || (markdown.isShouldTouchSpaceAndText() && !Markdown.spaceAndText(input, i, trigger.length()))) {
                // advance
                out.append(input, i, i + 1);
                i++;
                continue;
            }
            // find match and increase i
            final int triggerLen = trigger.length();
            final int contentStart = i + triggerLen;

            int lastFound = -1;

            for (int j = input.length() - triggerLen; j > i + 1; j--) {
                if (!input.startsWith(trigger, j)) continue;
                if (markdown.isShouldTouchSpaceAndText() && !Markdown.spaceAndText(input, j, triggerLen)) {
                    continue;
                }
                if (triggerLen > 1 && lastFound == j + 1) continue;
                lastFound = j;
            }
            if (lastFound == -1) {
                out.append(input, i, i + 1);
                i++;
                continue;
            }
            currentColors.addAll(convertFromStr(ChatColor.getLastColors(input.substring(0, lastFound))));
            List<ChatColor> colors = new ArrayList<>(currentColors);
            colors.add(markdown.getColor());
            out.append(markdown.getColor());
            if (keep) out.append(trigger);
            out.append(parse0(input.substring(contentStart, lastFound), keep, colors));
            if (keep) out.append(trigger);
            out.append(ChatColor.RESET);
            out.append(currentColors.stream()
                    .map(ChatColor::toString)
                    .collect(Collectors.joining()));
            i = lastFound + triggerLen;
        }
        return out.toString();
    }

}
