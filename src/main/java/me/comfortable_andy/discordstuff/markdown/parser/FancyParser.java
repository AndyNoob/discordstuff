package me.comfortable_andy.discordstuff.markdown.parser;

import me.comfortable_andy.discordstuff.markdown.Markdown;
import me.comfortable_andy.discordstuff.util.MarkdownRegion;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class FancyParser extends MarkdownParser {

    @SuppressWarnings("deprecation")
    @Override
    public String parse(final String input, boolean keepTriggers) {
        final Markdown[] markdowns = Markdown.getOrderedMarkdowns();
        final List<MarkdownRegion> incomplete = new ArrayList<>();
        final List<MarkdownRegion> completed = new ArrayList<>();

        int i = 0;

        character:
        while (i < input.length()) {
            final String left = input.substring(i);

            for (Markdown markdown : markdowns) {
                for (String trigger : markdown.getCharacters()) {
                    if (!left.startsWith(trigger)) continue;
                    if (markdown.isShouldTouchSpaceAndText()
                            && !Markdown.spaceAndText(input, i, trigger.length())
                    ) {
                        continue;
                    }

                    final MarkdownRegion matched = incomplete
                            .stream()
                            .filter(region -> region.getMarkdown() == markdown
                                    && region.getContent().equals(trigger))
                            .findFirst()
                            .orElse(null);

                    if (matched == null) {
                        incomplete.add(new MarkdownRegion(markdown, trigger, i));
                    } else {
                        if (trigger.length() > 1 && i + 1 < input.length() && input.substring(i + 1).startsWith(trigger)) continue;
                        matched.setCompleted(true);
                        // current index is on the first character of the trigger
                        // so moving it forward by the length of the trigger will
                        // put it on the letter after the last character of the trigger
                        matched.setEndExclusive(i + trigger.length());
                        incomplete.remove(matched);
                        completed.add(matched);
                    }

                    // the second loop is in charge of removing the triggers from the string

                    i += trigger.length();
                    continue character;
                }
            }
            i++;
        }

        if (completed.isEmpty()) return input;

        final StringBuilder builder = new StringBuilder();

        i = 0;

        character:
        while (i < input.length()) {
            for (MarkdownRegion region : completed) {
                if (!region.isCompleted()) continue;
                if (region.getStartInclusive() == i) {
                    builder.append(region.getMarkdown().getColor());
                    if (keepTriggers) builder.append(region.getContent());
                    i += region.getContent().length();
                    continue character;
                } else if (region.getEndExclusive() - region.getContent().length() == i) {
                    if (keepTriggers) builder.append(region.getContent());
                    builder.append(ChatColor.RESET);
                    completed.stream().filter(reg -> reg != region && reg.containsEnd(region)).forEach(reg -> builder.append(reg.getMarkdown().getColor()));
                    builder.append(ChatColor.getLastColors(input.substring(0, region.getEndExclusive())));
                    i += region.getContent().length();
                    continue character;
                }
            }

            builder.append(input.charAt(i++));
        }

        return builder.toString();
    }

}
