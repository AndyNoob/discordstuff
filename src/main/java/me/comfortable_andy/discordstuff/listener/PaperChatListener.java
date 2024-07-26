package me.comfortable_andy.discordstuff.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.stream.Collectors;

public class PaperChatListener extends ChatListener implements Listener {

    private static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.legacySection();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncChatEvent event) {
        final String newMessage = execute(
                event.getPlayer(),
                event.viewers().stream()
                        .filter(a -> a instanceof HumanEntity)
                        .map(a -> (HumanEntity) a).collect(Collectors.toSet()),
                SERIALIZER.serialize(event.message())
        );
        event.message(SERIALIZER.deserialize(newMessage));
    }
}
