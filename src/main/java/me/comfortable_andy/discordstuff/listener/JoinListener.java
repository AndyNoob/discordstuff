package me.comfortable_andy.discordstuff.listener;

import me.comfortable_andy.discordstuff.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class JoinListener implements Listener {

    private static final Method SET_COMPLETION;
    static {
        Method setCompletion;
        try {
            //noinspection JavaReflectionMemberAccess
            setCompletion = Player.class.getDeclaredMethod("setCustomChatCompletions", Collection.class);
            Main.getInstance().getLogger().info("@-ping tab completion is enabled.");
        } catch (NoSuchMethodException e) {
            setCompletion = null;
        }
        SET_COMPLETION = setCompletion;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (SET_COMPLETION == null) return;
        final List<String> pings = Bukkit.getOnlinePlayers().stream().map(p -> "@" + p.getName()).collect(Collectors.toList());
        for (Player player : Bukkit.getOnlinePlayers()) {
            try {
                SET_COMPLETION.invoke(player, pings);
            } catch (ReflectiveOperationException ignored) {
            }
        }
    }

}
