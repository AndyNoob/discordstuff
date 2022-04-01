package me.comfortable_andy.discordstuff.listener;

import me.comfortable_andy.discordstuff.markdown.Markdown;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        String input = event.getMessage();

        for (Markdown markdown : Markdown.MARKDOWNS.values()) {
            input = markdown.check(input);
        }

        event.setMessage(input);
    }
}
