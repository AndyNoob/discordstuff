package me.comfortable_andy.discordstuff;

import me.comfortable_andy.discordstuff.command.QuickCommand;
import me.comfortable_andy.discordstuff.command.commands.CommandShrug;
import me.comfortable_andy.discordstuff.command.commands.CommandTableflip;
import me.comfortable_andy.discordstuff.command.commands.CommandUnflip;
import me.comfortable_andy.discordstuff.listener.ChatListener;
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

    private final List<Class<? extends QuickCommand>> commands = Arrays.asList(CommandShrug.class
            , CommandTableflip.class, CommandUnflip.class);
    private final List<Class<? extends Markdown>> markdowns = Arrays.asList(MarkdownBold.class,
            MarkdownItalic.class, MarkdownUnderline.class, MarkdownStrikethrough.class);

    @Override
    public void onEnable() {
        INSTANCE = this;

        registerMarkdowns();
        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerMarkdowns() {
        for (Class<? extends Markdown> clazz : markdowns) {
            try {
                clazz.newInstance();
            } catch (ReflectiveOperationException e) {
                getLogger().warning("Unable to register \"" + clazz.getSimpleName() + "\"!");
            }
        }
    }

    private void registerCommands() {
        for (Class<? extends QuickCommand> clazz : commands) {
            try {
                clazz.newInstance();
            } catch (Exception e) {
                getLogger().warning("Unable to register \"" + clazz.getSimpleName() + "\"!");
            }
        }
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
    }

    public static Main getInstance() {
        return INSTANCE;
    }
}
