package me.comfortable_andy.discordstuff.markdown.parser;

import me.comfortable_andy.discordstuff.markdown.Markdown;
import me.comfortable_andy.discordstuff.util.StringUtil;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/*
 * a.k.a. the discord way
 * keynotes for reverse engineering:
 * - there seems to be a special case for "*" that it will not be counted
 *   AS STARTING TRIGGER if it is followed by a space, everything else doesn't care
 * - when a markdown trigger is found, the next closest trigger of the same
 *   is searched for and the text in between is parsed recursively
 */
public class DiscordParser extends MarkdownParser {

    @Override
    public String parse(String input, boolean keepTriggers) {
        return this.parse0(input, keepTriggers, Collections.emptyList());
    }

    public String parse0(String input, boolean keep, List<ChatColor> currentColors) {
        final StringBuilder builder = new StringBuilder();
        final Matcher matcher = getPattern().matcher(input);

        String left = input;

        while (matcher.find()) {
            final int trueStart = matcher.start();
            final int contentStart = matcher.start(2);
            final int contentEnd = matcher.end(2);
            final int trueEnd = matcher.end();
            final ChatColor color = findColor(matcher.group(1));
            final List<ChatColor> colors = new ArrayList<>(currentColors);
            colors.add(color);
            builder
                    .append(left, 0, keep ? contentStart : trueStart)
                    .append(color)
                    .append(this.parse0(matcher.group(2), keep, colors))
                    .append(left, keep ? contentEnd : 0, keep ? trueEnd : 0)
                    .append(ChatColor.RESET)
                    .append(currentColors.stream().map(ChatColor::toString).collect(Collectors.joining()));
            left = left.substring(trueEnd);
            matcher.reset(left);
        }

        builder.append(left);

        return builder.toString();
    }

    private ChatColor findColor(String trigger) {
        return Arrays.stream(Markdown.getOrderedMarkdowns())
                .filter(markdown -> Arrays.asList(markdown.getCharacters()).contains(trigger))
                .findFirst()
                .orElseThrow(IllegalStateException::new)
                .getColor();
    }

    private Pattern getPattern() {
        final Markdown[] markdowns = Markdown.getOrderedMarkdowns();
        final String all = concatenate(markdowns);
        final String specials = concatenate(Arrays.stream(markdowns).filter(Markdown::isMustHaveSpaceAfter).toArray(Markdown[]::new));

        return Pattern.compile(String.format("(%s)((?<=%s)\\S.+?|(?<!%s).+?)(\\1)", all, specials, specials));
    }

    private String concatenate(Markdown[] markdowns) {
        return Arrays.stream(markdowns)
                .reduce(
                        "",
                        (s, markdown) -> s + (s.isEmpty() ? "" : "|") + Arrays.stream(markdown.getCharacters()).map(StringUtil::regexEscape).collect(Collectors.joining("|")),
                        (s1, s2) -> s1 + "|" + s2
                );
    }

}
