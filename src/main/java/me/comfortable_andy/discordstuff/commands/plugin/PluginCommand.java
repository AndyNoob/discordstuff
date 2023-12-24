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

@SuppressWarnings({"CodeBlock2Expr", "SameParameterValue"})
public class PluginCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        runIf(permCheck(sender, "reload") && matchArg(0, "reload", args), () -> {
            Main.getInstance().reloadConfig();
            sender.sendMessage(Markdown.convert("__Re__**loaded**!"));
        });
        runIf(permCheck(sender, "test") && matchArg(0, "test", args), () -> {
            final String input = String.join(" ", collect(1, args));
            sender.sendMessage("Output in " + ChatColor.GREEN + Markdown.getParser().name().toLowerCase() + ChatColor.RESET + ChatColor.GRAY + " ==>" + ChatColor.RESET + " " + Markdown.convert(input) + ChatColor.GRAY + " <== (" + ChatColor.RESET + Markdown.convert(input, true) + ChatColor.GRAY + ")");
        });
        runIf(permCheck(sender, "parser") && matchArg(0, "parser", args), () -> {
            final String parser = getArg(1, args);

            if (parser.isEmpty()) {
                sender.sendMessage(ChatColor.BOLD + "Current parser: " + ChatColor.GREEN + Markdown.getParser().name().toLowerCase());
                return;
            }

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
            runIf(permCheck(commandSender, "parser"), () -> {
                result.add("parser");
            });
        });
        runIf(strings.length == 2 && matchArg(0, "parser", strings), () -> {
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
