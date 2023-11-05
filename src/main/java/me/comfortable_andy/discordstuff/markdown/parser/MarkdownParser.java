package me.comfortable_andy.discordstuff.markdown.parser;

import lombok.Getter;

import java.util.function.Supplier;

public abstract class MarkdownParser {

    public String parse(String input) {
        return parse(input, false);
    }

    public abstract String parse(String input, boolean keepTriggers);

    private static class OffParser extends MarkdownParser {
        @Override
        public String parse(String input, boolean keepTriggers) {
            return input;
        }
    }

    @Getter
    public enum Type {
        FANCY(FancyParser::new),
        DISCORD(DiscordParser::new),
        OFF(OffParser::new)
        ;

        private final Supplier<MarkdownParser> supplier;

        Type(Supplier<MarkdownParser> supplier) {
            this.supplier = supplier;
        }

        public static Type find(String name) {
            for (Type type : values()) {
                if (type.name().equalsIgnoreCase(name)) return type;
            }
            return null;
        }

    }

}
