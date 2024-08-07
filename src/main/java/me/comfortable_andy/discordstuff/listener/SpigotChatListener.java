package me.comfortable_andy.discordstuff.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class SpigotChatListener extends ChatListener implements Listener {

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        final String newMsg = execute(event.getPlayer(), event.getRecipients(), event.getMessage());
        event.setMessage(newMsg);
    }

}
