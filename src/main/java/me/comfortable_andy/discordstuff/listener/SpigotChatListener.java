package me.comfortable_andy.discordstuff.listener;

import me.comfortable_andy.discordstuff.markdown.Markdown;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class SpigotChatListener extends ChatListener implements Listener {

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        String converted = Markdown.convert(event.getMessage());
        converted = execute(event.getPlayer(), event.getRecipients(), converted);
        event.setMessage(converted);
    }

}
