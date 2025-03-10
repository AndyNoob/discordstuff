package me.comfortable_andy.discordstuff.commands.plugin;

import me.comfortable_andy.discordstuff.DiscordStuffMain;
import me.comfortable_andy.discordstuff.markdown.Markdown;
import me.comfortable_andy.discordstuff.markdown.parser.MarkdownParser;
import me.comfortable_andy.discordstuff.util.EmojiUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"CodeBlock2Expr", "SameParameterValue", "deprecation"})
public class PluginCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
        if (permCheck(sender, "reload")
                && matchArg(0, "reload", args)) {
            DiscordStuffMain.getInstance().reloadConfig();
            sender.sendMessage(Markdown.convert("__Re__**loaded**!"));
        }
        if (permCheck(sender, "test")
                && matchArg(0, "test", args)) {
            final String input = String.join(" ", collect(1, args));
            sender.sendMessage("Output in " + ChatColor.GREEN + Markdown.getParser().name().toLowerCase() + ChatColor.RESET + ChatColor.GRAY + " ==>" + ChatColor.RESET + " " + Markdown.convert(input) + ChatColor.GRAY + " <== (" + ChatColor.RESET + Markdown.convert(input, true) + ChatColor.GRAY + ")");
        }
        if (permCheck(sender, "parser") && matchArg(0, "parser", args)) {
            final String parser = getArg(1, args);

            if (parser.isEmpty()) {
                sender.sendMessage(ChatColor.BOLD + "Current parser: " + ChatColor.GREEN + Markdown.getParser().name().toLowerCase());
                return true;
            }

            final MarkdownParser.Type type = MarkdownParser.Type.find(parser.toUpperCase());
            if (type == null) {
                sender.sendMessage(ChatColor.BOLD + "Unknown parser: " + ChatColor.RED + parser);
            } else {
                DiscordStuffMain.getInstance().getConfig().set("parser", type.name());
                DiscordStuffMain.getInstance().saveConfig();
                sender.sendMessage(ChatColor.BOLD + "Set parser to: " + ChatColor.GREEN + type.name().toLowerCase());
            }
        }
        if (permCheck(sender, "emoji") && matchArg(0, "emoji", args)) {
            if (matchArg(1, "download", args)) {
                EmojiUtil.downloadEmojis(DiscordStuffMain.getInstance(), () -> {
                    EmojiUtil.loadEmojis(DiscordStuffMain.getInstance());
                    sender.sendMessage(Markdown.convert("**Done**!"));
                });
            }
            if (matchArg(1, "show", args)) {
                int perMsg = 100;
                List<String> all = new ArrayList<>(new HashSet<>(EmojiUtil.getEmojis().values()));

                var groups = all
                        .stream()
                        .filter(v -> v.length() <= 2)
                        .collect(Collectors
                                .groupingBy(v -> all.indexOf(v) * perMsg / all.size())
                        );
                for (var set : groups.entrySet()) {
                    String msg = set.getKey() + ": " + String.join(" ", set.getValue());
                    sender.sendMessage(Component.text(msg));
                    DiscordStuffMain.getInstance().getLogger().info(msg);
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] strings) {
        final List<String> result = new ArrayList<>();
        runIf(strings.length == 1, () -> {
            runIf(permCheck(sender, "reload"), () -> {
                result.add("reload");
            });
            runIf(permCheck(sender, "test"), () -> {
                result.add("test");
            });
            runIf(permCheck(sender, "parser"), () -> {
                result.add("parser");
            });
            runIf(permCheck(sender, "emoji"), () -> {
                result.add("emoji");
            });
        });
        runIf(strings.length == 2 && matchArg(0, "parser", strings), () -> {
            for (MarkdownParser.Type type : MarkdownParser.Type.values()) {
                result.add(type.name());
            }
        });
        runIf(strings.length == 2 && matchArg(0, "emoji", strings), () -> {
            result.add("show");
            result.add("download");
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
