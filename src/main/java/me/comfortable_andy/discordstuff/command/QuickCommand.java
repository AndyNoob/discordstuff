package me.comfortable_andy.discordstuff.command;

import me.comfortable_andy.discordstuff.Main;
import me.comfortable_andy.discordstuff.util.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class QuickCommand extends BukkitCommand {

    static {
        try {
            commandMap = ReflectionUtil.getField(Bukkit.getServer(), "commandMap");
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    private static SimpleCommandMap commandMap;

    private final String message;

    protected QuickCommand(String name, String message) {
        super(name);

        register(commandMap);
        commandMap.register(Main.getInstance().getName().toLowerCase(), this);

        this.message = message;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        Consumer<String> send;

        if (!(sender instanceof Player)) {
            send = Bukkit::broadcastMessage;
        } else {
            send = s -> ((Player) sender).chat(s);
        }

        if (args.length == 0) {
            send.accept(message);
        } else {
            StringBuilder builder = new StringBuilder();

            for (String arg : args) {
                builder.append(arg).append(" ");
            }

            builder.append(message);
            send.accept(builder.toString());
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return new ArrayList<>();
    }
}
