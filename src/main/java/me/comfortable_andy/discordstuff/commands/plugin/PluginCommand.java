package me.comfortable_andy.discordstuff.commands.plugin;

import me.comfortable_andy.discordstuff.Main;
import me.comfortable_andy.discordstuff.markdown.Markdown;
import me.comfortable_andy.discordstuff.markdown.parser.MarkdownParser;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PluginCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        runIf(permCheck(sender, "reload") && matchArg(0, "reload", args), () -> {
            Main.getInstance().reloadConfig();
            sender.sendMessage(Markdown.convert("__Re__**loaded**!"));
        });
        runIf(permCheck(sender, "test") && matchArg(0, "test", args), () -> {
            sender.sendMessage("Output in " + Markdown.getParser().getClass().getSimpleName() + ChatColor.BOLD + " ==>" + ChatColor.RESET + " " + Markdown.convert(String.join(" ", collect(1, args))) + ChatColor.BOLD + "<==");
        });
        runIf(permCheck(sender, "switch") && matchArg(0, "switch", args), () -> {
            final String parser = getArg(1, args);
            final MarkdownParser.Type type = MarkdownParser.Type.find(parser.toUpperCase());
            if (type == null) {
                sender.sendMessage(ChatColor.BOLD + "Unknown parser: " + ChatColor.RED + parser);
            } else {
                Main.getInstance().getConfig().set("parser", type.name());
                Main.getInstance().saveConfig();
                sender.sendMessage(ChatColor.BOLD + "Set parser to: " + ChatColor.GREEN + type.name().toLowerCase());
            }
        });
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        final List<String> result = new ArrayList<>();
        runIf(strings.length == 1, () -> {
            runIf(permCheck(commandSender, "reload"), () -> {
                result.add("reload");
            });
            runIf(permCheck(commandSender, "test"), () -> {
                result.add("test");
            });
            runIf(permCheck(commandSender, "switch"), () -> {
                result.add("switch");
            });
        });
        runIf(strings.length == 2 && matchArg(0, "switch", strings), () -> {
            for (MarkdownParser.Type type : MarkdownParser.Type.values()) {
                result.add(type.name());
            }
        });
        return StringUtil.copyPartialMatches(latest(strings), result, new ArrayList<>());
    }

    private boolean permCheck(CommandSender sender, String perm) {
        return sender.hasPermission("discordstuff.commands.plugin." + perm);
    }

    private void runIf(boolean condition, Runnable runnable) {
        if (condition) runnable.run();
    }

    private String getArg(int index, String... args) {
        return args.length > index ? args[index] : "";
    }

    private boolean matchArg(int index, String val, String... args) {
        return getArg(index, args).equalsIgnoreCase(val);
    }

    private String[] collect(int from, String... args) {
        return from <= args.length ? Arrays.copyOfRange(args, from, args.length) : new String[0];
    }

    private String latest(String... args) {
        return getArg(args.length - 1, args);
    }

}
