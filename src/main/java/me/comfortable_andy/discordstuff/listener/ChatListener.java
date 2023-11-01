package me.comfortable_andy.discordstuff.listener;

import me.comfortable_andy.discordstuff.markdown.Markdown;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setMessage(Markdown.convert(event.getMessage()));
    }
}
