package me.comfortable_andy.discordstuff;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.comfortable_andy.discordstuff.commands.discord.AppendingCommand;
import me.comfortable_andy.discordstuff.commands.discord.CommandShrug;
import me.comfortable_andy.discordstuff.commands.discord.CommandTableflip;
import me.comfortable_andy.discordstuff.commands.discord.CommandUnflip;
import me.comfortable_andy.discordstuff.commands.plugin.PluginCommand;
import me.comfortable_andy.discordstuff.listener.PaperChatListener;
import me.comfortable_andy.discordstuff.listener.SpigotChatListener;
import me.comfortable_andy.discordstuff.listener.JoinListener;
import me.comfortable_andy.discordstuff.markdown.Markdown;
import me.comfortable_andy.discordstuff.markdown.markdowns.MarkdownBold;
import me.comfortable_andy.discordstuff.markdown.markdowns.MarkdownItalic;
import me.comfortable_andy.discordstuff.markdown.markdowns.MarkdownStrikethrough;
import me.comfortable_andy.discordstuff.markdown.markdowns.MarkdownUnderline;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public final class Main extends JavaPlugin {

    private static Main INSTANCE;

    private final List<Class<? extends AppendingCommand>> commands = Arrays.asList(CommandShrug.class, CommandTableflip.class, CommandUnflip.class);
    private final List<Class<? extends Markdown>> markdowns = Arrays.asList(MarkdownBold.class,
            MarkdownItalic.class, MarkdownUnderline.class, MarkdownStrikethrough.class);

    @Override
    public void onEnable() {
        INSTANCE = this;

        saveDefaultConfig();

        getCommand("discordstuff").setExecutor(new PluginCommand());

        makeInstance(this.markdowns);
        makeInstance(this.commands);
        registerListeners();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private <T> void makeInstance(List<Class<? extends T>> classes) {
        for (Class<?> clazz : classes) {
            try {
                clazz.newInstance();
            } catch (ReflectiveOperationException e) {
                getLogger().warning("Unable to register \"" + clazz.getSimpleName() + "\"!");
            }
        }
    }

    private void registerListeners() {
        try {
            getLogger().info("Located " + AsyncChatEvent.class.getName() + ", registering paper chat listener.");
            getServer().getPluginManager().registerEvents(new PaperChatListener(), this);
        } catch (Exception e) {
            getLogger().info("Registering spigot chat listener.");
            getServer().getPluginManager().registerEvents(new SpigotChatListener(), this);
        }
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
    }

    public static Main getInstance() {
        return INSTANCE;
    }
}
