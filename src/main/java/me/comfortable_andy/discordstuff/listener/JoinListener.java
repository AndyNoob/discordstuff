package me.comfortable_andy.discordstuff.listener;

import me.comfortable_andy.discordstuff.util.EmojiUtil;
import me.comfortable_andy.discordstuff.util.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collections;
import java.util.List;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws ReflectiveOperationException {
        if (ReflectionUtil.ADD_COMPLETION == null) return;
        String ping = "@" + event.getPlayer().getName();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("discordstuff.ping.use"))
                ReflectionUtil.ADD_COMPLETION.invoke(player, Collections.
        singleton(ping));
        }
        final List<String> pings = Bukkit.getOnlinePlayers()
                .stream()
                .map(p -> "@" + p.getName())
                .toList();
        ReflectionUtil.ADD_COMPLETION.invoke(event.getPlayer(), pings);
        if (event.getPlayer().hasPermission("discordstuff.emoji.use"))
            EmojiUtil.updateEmojiCompletions(event.getPlayer(), false);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) throws ReflectiveOperationException {
        if (ReflectionUtil.REMOVE_COMPLETION == null) return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            ReflectionUtil.REMOVE_COMPLETION.invoke(player, "@" + event.getPlayer().getName());
        }
    }

}
