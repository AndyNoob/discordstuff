package me.comfortable_andy.discordstuff.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StringUtilTest {

    @Test
    public void forwardCheck() {
        assertTrue(StringUtil.forwardMatches("This is a test", 0, true, "This is a test"));
        assertTrue(StringUtil.forwardMatches("i!dsgsdgsdgds", 0, false, "!"));
        assertTrue(StringUtil.forwardMatches("i!sdgsdgsgdssd", 1, true, "[^\\s]"));
    }

    @Test
    public void backwardCheck() {
        assertTrue(StringUtil.backwardMatches("This is a test", 13, true, "test"));
        assertFalse(StringUtil.backwardMatches("This is a test", 14, true, "test"));
    }


}