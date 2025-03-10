package me.comfortable_andy.discordstuff.listener;

import me.comfortable_andy.discordstuff.DiscordStuffMain;
import me.comfortable_andy.discordstuff.markdown.Markdown;
import me.comfortable_andy.discordstuff.util.EmojiUtil;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.intellij.lang.annotations.Subst;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class ChatListener {

    @SuppressWarnings("deprecation")
    protected String execute(Player player, Set<? extends HumanEntity> recipients, String str) {
        str = Markdown.convert(str);
        final FileConfiguration config = DiscordStuffMain.getInstance().getConfig();

        if (player.hasPermission("discordstuff.ping.use")
                && config.getBoolean("ping.enabled", true)) {
            final List<String> list = config
                    .getStringList("ping.colors");

            if (list.isEmpty()) list.add("BLUE");

            final String replacement = list
                    .stream()
                    .map(ChatColor::valueOf)
                    .reduce("", (st, col) -> st + col, (a, b) -> a + b) + "$0";

            for (HumanEntity recipient : recipients) {
                final Pattern pattern = Pattern.compile("@?" + recipient.getName());
                final Matcher matcher = pattern.matcher(str);
                boolean matched = false;
                while (matcher.find()) {
                    matched = true;
                    final String lastCol = ChatColor.getLastColors(str.substring(0, matcher.start()));
                    str = matcher.replaceFirst(replacement + ChatColor.RESET + lastCol);
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

        if (player.hasPermission("discordstuff.emoji.use")
                && config.getBoolean("emoji.enabled", true)) {
            AtomicReference<String> strAtomic = new AtomicReference<>(str);
            List<String> whitelist = config.getStringList("emoji.whitelist");
            var set = EmojiUtil.getEmojis().entrySet()
                    .stream()
                    .parallel()
                    .filter(e -> !config.getBoolean("emoji.colonOnly", false)
                            || e.getKey().startsWith(":"))
                    .filter(e -> whitelist.isEmpty() || whitelist.contains(e.getKey()))
                    .filter(e -> strAtomic.get().contains(e.getKey()))
                    .sorted(Comparator
                            .comparing((Map.Entry<String, String> e) -> e.getKey().length())
                            .reversed())
                    .collect(Collectors.toList());
            for (var entry : set) {
                str = str.replace(entry.getKey(), entry.getValue());
            }
        }

        return str;
    }

}
