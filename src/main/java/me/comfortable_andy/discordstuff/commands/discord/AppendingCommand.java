package me.comfortable_andy.discordstuff.commands.discord;

import me.comfortable_andy.discordstuff.DiscordStuffMain;
import me.comfortable_andy.discordstuff.markdown.Markdown;
import me.comfortable_andy.discordstuff.util.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;

public abstract class AppendingCommand extends BukkitCommand {

    static {
        try {
            commandMap = ReflectionUtil.getField(Bukkit.getServer(), "commandMap");
        } catch (ReflectiveOperationException e) {
            DiscordStuffMain.getInstance().getLogger().log(Level.SEVERE, "Could not retrieve the command map!", e);
        }
    }

    private static SimpleCommandMap commandMap;

    private final String message;
    protected final Permission permission;

    protected AppendingCommand(String name, String message) {
        super(name);

        commandMap.register(DiscordStuffMain.getInstance().getName().toLowerCase(), this);

        this.message = message;

        DiscordStuffMain.getInstance().getServer().getPluginManager().addPermission(
                this.permission = new Permission(DiscordStuffMain.getInstance().getName() + ".commands." + name, PermissionDefault.TRUE)
        );
        this.permission.addParent(DiscordStuffMain.getInstance().getServer().getPluginManager().getPermission("discordstuff.commands.*"), true);
    }

    public boolean isEnabled() {
        return DiscordStuffMain.getInstance().getConfig().getBoolean("commands." + getName() + ".enabled", true);
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.isEnabled()) return false;
        if (!sender.hasPermission(this.permission)) return false;

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
            send.accept(Markdown.convert(builder.toString()) + message);
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return new ArrayList<>();
    }

    @Override
    public boolean testPermissionSilent(CommandSender target) {
        return !this.isEnabled() || super.testPermissionSilent(target);
    }

}
