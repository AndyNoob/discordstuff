package me.comfortable_andy.discordstuff.markdown;

import me.comfortable_andy.discordstuff.markdown.Markdown;
import me.comfortable_andy.discordstuff.markdown.parser.MarkdownParser;
import me.comfortable_andy.discordstuff.util.MarkdownRegion;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DumbParser extends MarkdownParser {
    @Override
    public String parse(String input, boolean keepTriggers) {
        final List<MarkdownRegion> regions = new ArrayList<>();
        final Map<Integer, List<Markdown>> onHolds = new HashMap<>();
        final Markdown[] markdowns = Markdown.getOrderedMarkdowns();

        String str = input;
        StringBuilder builder = new StringBuilder();

        while (!str.isEmpty()) {
            final int index = builder.lastIndexOf("");
            final List<Markdown> current = new ArrayList<>();
            boolean found = false;

            for (Markdown markdown : markdowns) {
                if (str.isEmpty()) break;

                for (String matching : markdown.getCharacters()) {
                    if (!str.startsWith(matching)) continue;

                    found = true;

                    final MarkdownRegion region = unHoldClosable(onHolds, index, markdown, matching);
                    if (region == null) current.add(markdown);
                    else regions.add(region);

                    if (str.length() <= matching.length()) {
                        str = "";
                        break;
                    }

                    str = str.substring(matching.length());
                    break;
                }
            }

            if (found) onHolds.put(index, current);
            if (str.isEmpty()) break;

            builder.append(str.charAt(0));
            if (str.length() > 1)
                str = str.substring(1);
            else break;
        }

        str = builder.toString();
        builder = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            for (MarkdownRegion region : regions) {
                if (region.getStartInclusive() == i)
                    builder.append(region.getMarkdown().getColor());
                else if (region.getEndExclusive() == i) {
                    builder.append(ChatColor.RESET);

                    for (MarkdownRegion reg : regions) {
                        if (reg == region) continue;
                        if (reg.containsEnd(region)) {
                            builder.append(reg.getMarkdown().getColor());
                        }
                    }
                }
            }

            if (onHolds.containsKey(i)) {
                for (Markdown markdown : onHolds.get(i)) {
                    builder.append(markdown.getColor());
                }
            }

            builder.append(str.charAt(i));
        }

        return builder.toString();
    }

    private MarkdownRegion unHoldClosable(Map<Integer, List<Markdown>> onHolds, int currentIndex, Markdown markdown, String matching) {
        for (Map.Entry<Integer, List<Markdown>> entry : onHolds.entrySet()) {
            final int index = entry.getKey();
            final List<Markdown> markdowns = entry.getValue();

            if (markdowns.remove(markdown)) {
                onHolds.put(index, markdowns);
                return new MarkdownRegion(markdown, matching, index, currentIndex);
            }
        }

        return null;
    }
}
