package me.comfortable_andy.discordstuff.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@SuppressWarnings({"unused", "MismatchedQueryAndUpdateOfCollection"})
public class EmojiUtil {

    public static final String EMOJI_FILE_NAME = "emojis.json5";
    private static final Map<String, String> EMOJIS = Collections.synchronizedMap(new LinkedHashMap<>());
    private static final Gson GSON = new GsonBuilder().setLenient().disableHtmlEscaping().create();
    private static final URL DATA_URL;

    static {
        try {
            DATA_URL = new URL("https://emzi0767.mzgit.io/discord-emoji/discordEmojiMap.min.json");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private static URL url;

    public static void updateEmojiCompletions(boolean remove) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateEmojiCompletions(player, remove);
        }
    }

    public static void updateEmojiCompletions(Player player, boolean remove) {
        if (remove) {
            try {
                ReflectionUtil.REMOVE_COMPLETION.invoke(player, EMOJIS.keySet());
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                ReflectionUtil.ADD_COMPLETION.invoke(player, EMOJIS.keySet());
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void loadEmojis(Plugin plugin) {
        File file = new File(plugin.getDataFolder(), EMOJI_FILE_NAME);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.getLogger().info("Downloading emoji data from https://emzi0767.mzgit.io/discord-emoji/");
            downloadEmojis(plugin, () -> loadEmojis(plugin));
            return;
        }
        EmojiUtil.updateEmojiCompletions(true);
        EMOJIS.clear();
        try {
            FileReader reader = new FileReader(file);
            EmojiData data = GSON.fromJson(reader, EmojiData.class);
            for (EmojiDefinition definition : data.emojiDefinitions) {
                for (String name : definition.namesWithColons) {
                    int maxLen = Math.max(
                            definition.surrogates.length(),
                            definition.surrogatesAlternate == null ? 0 : definition.surrogatesAlternate.length()
                    );
                    if (maxLen > 2) continue; // minecraft can't display surrogates > 2
                    String surrogate;
                    if (definition.surrogatesAlternate == null
                            || (definition.surrogates.length() < definition.surrogatesAlternate.length())) {
                        surrogate = (definition.surrogates);
                    } else surrogate = (definition.surrogatesAlternate);
                    EMOJIS.put(name, surrogate);
                }
            }
            plugin.getLogger().info("Loaded " + EMOJIS.size() + " emoji definitions, purging " + (data.emojiDefinitions.size() - EMOJIS.size()) + " due to Minecraft client rendering limitations.");
            updateEmojiCompletions(false);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({"deprecation", "ResultOfMethodCallIgnored"})
    public static void downloadEmojis(Plugin plugin, Runnable callback) {
        String userAgent = "DiscordStuff v" + plugin.getDescription().getVersion();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            HttpURLConnection connection = null;
            try {
                URL url = DATA_URL;
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", userAgent);
                connection.setUseCaches(false);
                connection.connect();

                Scanner scanner = new Scanner(connection.getInputStream()).useDelimiter("\\A");
                if (!scanner.hasNext()) throw new IllegalStateException("did not receive emoji data from Github");
                File file = new File(plugin.getDataFolder(), EMOJI_FILE_NAME);
                if (file.exists()) file.delete();
                else file.getParentFile().mkdirs();
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("// file downloaded by " + userAgent + " at " + ZonedDateTime.now() + " from " + url + "\n\n");
                    writer.write(scanner.next());
                }
                callback.run();
            } catch (IOException e) {
                throw new RuntimeException("could not load emojis", e);
            } finally {
                if (connection != null) connection.disconnect();
            }
        });
    }

    public static Map<String, String> getEmojis() {
        return Collections.unmodifiableMap(EMOJIS);
    }

    private static class EmojiData {
        private List<EmojiDefinition> emojiDefinitions;
    }

    private static class EmojiDefinition {
        private String surrogates;
        private String surrogatesAlternate;
        private List<String> namesWithColons;
    }

}
