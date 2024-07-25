package me.comfortable_andy.discordstuff.listener;

import me.comfortable_andy.discordstuff.Main;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.intellij.lang.annotations.Subst;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ChatListener {

    protected String execute(Player player, Set<? extends HumanEntity> recipients, String converted) {
        final FileConfiguration config = Main.getInstance().getConfig();

        if (player.hasPermission("discordstuff.ping.use")
                && config.getBoolean("ping.enabled", true)) {
            final List<String> list = config
                    .getStringList("ping.colors");

            if (list.isEmpty()) list.add("BLUE");

            final String replacement = list
                    .stream()
                    .map(ChatColor::valueOf)
                    .reduce("", (str, col) -> str + col, (a, b) -> a + b) + "$0";

            for (HumanEntity recipient : recipients) {
                final Pattern pattern = Pattern.compile("@?" + recipient.getName());
                final Matcher matcher = pattern.matcher(converted);
                boolean matched = false;
                while (matcher.find()) {
                    matched = true;
                    final String lastCol = ChatColor.getLastColors(converted.substring(0, matcher.start()));
                    converted = matcher.replaceFirst(replacement + ChatColor.RESET + lastCol);
                }
                if (matched) {
                    @Subst("minecraft:entity.arrow.hit_player") final String sound = config.getString("ping.sound.name", "minecraft:entity.arrow.hit_player");

                    if (!sound.isEmpty()) {
                        final Location loc = recipient.getEyeLocation();
                        final SoundCategory category = SoundCategory.valueOf(config.getString("ping.sound.type", "MASTER"));
                        final float volume = (float) config.getDouble("ping.sound.volume", 1);
                        final float pitch = (float) config.getDouble("ping.sound.pitch", 1);
                        if (recipient instanceof Player) {
                            ((Player) recipient).playSound(
                                    loc,
                                    sound,
                                    category,
                                    volume,
                                    pitch
                            );
                        } else {
                            recipient.playSound(
                                    Sound.sound(Key.key(sound), category, volume, pitch),
                                    loc.getX(),
                                    loc.getY(),
                                    loc.getZ()
                            );
                        }
                    }
                }
            }
        }
        return converted;
    }

}
