package me.comfortable_andy.discordstuff.listener;

import me.comfortable_andy.discordstuff.Main;
import me.comfortable_andy.discordstuff.markdown.Markdown;
import org.bukkit.ChatColor;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        String converted = Markdown.convert(event.getMessage());
        final FileConfiguration config = Main.getInstance().getConfig();

        if (event.getPlayer().hasPermission("discordstuff.ping.use")
                && config.getBoolean("ping.enabled", true)) {
            final List<String> list = config
                    .getStringList("ping.colors");

            if (list.isEmpty()) list.add("BLUE");

            final String replacement = list
                    .stream()
                    .map(ChatColor::valueOf)
                    .reduce("", (str, col) -> str + col, (a, b) -> a + b) + "$0";

            for (Player player : event.getRecipients()) {
                final Pattern pattern = Pattern.compile("@?" + player.getName());
                final Matcher matcher = pattern.matcher(converted);
                boolean matched = false;
                while (matcher.find()) {
                    matched = true;
                    final String lastCol = ChatColor.getLastColors(converted.substring(0, matcher.start()));
                    converted = matcher.replaceFirst(replacement + ChatColor.RESET + lastCol);
                }
                if (matched) {
                    final String sound = config.getString("ping.sound.name", "minecraft:entity.arrow.hit_player");

                    if (!sound.isEmpty())
                        player.playSound(
                                player.getEyeLocation(),
                                sound,
                                SoundCategory.valueOf(config.getString("ping.sound.type", "MASTER")),
                                (float) config.getDouble("ping.sound.volume", 1),
                                (float) config.getDouble("ping.sound.pitch", 1)
                        );
                }
            }
        }

        event.setMessage(converted);
    }

}
