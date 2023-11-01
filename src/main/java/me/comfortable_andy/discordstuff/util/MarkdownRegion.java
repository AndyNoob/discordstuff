package me.comfortable_andy.discordstuff.util;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import me.comfortable_andy.discordstuff.markdown.Markdown;

@Accessors(chain = true)
@Getter
@Setter
@ToString
public class MarkdownRegion {

    private final Markdown markdown;
    private final String content;
    private final int startInclusive;
    private int endExclusive;
    private boolean completed;

    public MarkdownRegion(Markdown markdown, String content, int startInclusive) {
        this.markdown = markdown;
        this.content = content;
        this.startInclusive = startInclusive;
        this.completed = false;
    }

    public MarkdownRegion(Markdown markdown, String content, int startInclusive, int endExclusive) {
        this.markdown = markdown;
        this.content = content;
        this.startInclusive = startInclusive;
        this.endExclusive = endExclusive;
        this.completed = true;
    }

    private void checkCompletion() {
        if (!completed) throw new IllegalStateException("This region is not yet completed!");
    }

    public boolean contains(int index) {
        checkCompletion();
        return index >= startInclusive && index < endExclusive;
    }

    public boolean containsEnd(MarkdownRegion region) {
        checkCompletion();
        return contains(region.endExclusive - 1);
    }

}
