package me.comfortable_andy.discordstuff.markdown.parser;

import me.comfortable_andy.discordstuff.markdown.Markdown;
import me.comfortable_andy.discordstuff.util.MarkdownRegion;
import me.comfortable_andy.discordstuff.util.StringUtil;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class FancyParser extends MarkdownParser {

    @Override
    public String parse(String input, boolean keepTriggers) {
        final Markdown[] markdowns = Markdown.getOrderedMarkdowns();
        final List<MarkdownRegion> regions = new ArrayList<>();
        final StringBuilder builder = new StringBuilder();

        String left = input;
        char last = ' ';

        while (!left.isEmpty()) {
            for (Markdown markdown : markdowns) {
                for (String s : markdown.getCharacters()) {
                    if (left.length() < s.length()) continue;

                    // for the markdown to be valid, both tags must be next to nons-paces
                    // so "~~example~~" is valid, but "~~ example ~~" is not

                    final int first = left.indexOf(s);
                    if (first != 0) continue;

                    final MarkdownRegion region = regions.stream().filter(reg -> !reg.isCompleted()).filter(reg -> reg.getContent().equals(s)).findFirst().orElse(null);

                    if (region == null) {
                        // still allow usage if it's not closed
                        if (!StringUtil.forwardMatches(left, s.length(), true, "[^\\s]")) continue;

                        final int closing = left.indexOf(s, first + s.length());
                        if (closing == -1) continue;
                        if (!StringUtil.backwardMatches(left, closing, false, "[^\\s]")) continue;

                        left = left.substring(s.length());
                        builder.append(markdown.getColor());
                        if (keepTriggers) builder.append(s);
                        regions.add(new MarkdownRegion(markdown, s, builder.lastIndexOf("")));
                    } else {
                        // wasn't the one we found when recording opening one
                        if (Character.isWhitespace(last)) continue;

                        left = left.substring(s.length());
                        if (keepTriggers) builder.append(s);
                        builder.append(ChatColor.RESET);
                        regions.stream()
                                .filter(reg -> reg != region)
                                .filter(reg ->
                                        reg.isCompleted() ? reg.contains(builder.lastIndexOf("")) : reg.getStartInclusive() < builder.lastIndexOf("")
                                )
                                .forEach(reg -> builder.append(reg.getMarkdown().getColor()));
                        region.setEndExclusive(builder.lastIndexOf(""));
                        region.setCompleted(true);
                    }
                }
            }

            if (left.isEmpty()) break;

            last = left.charAt(0);
            builder.append(last);
            left = left.substring(1);
        }

        return builder.toString();
    }
}
