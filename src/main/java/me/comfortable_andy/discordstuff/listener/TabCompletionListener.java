package me.comfortable_andy.discordstuff.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;

public class TabCompletionListener implements Listener {

    @EventHandler
    public void onTabComplete(TabCompleteEvent event) {
        event.getSender().sendMessage("Tab complete event: " + event.getBuffer());
    }

}
