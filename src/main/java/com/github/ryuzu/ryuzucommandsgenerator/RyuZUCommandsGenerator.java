package com.github.ryuzu.ryuzucommandsgenerator;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.stream.Collectors;

public class RyuZUCommandsGenerator {
    private static JavaPlugin plugin;
    public static CommandsExecuter executer = new CommandsExecuter();
    public static String resistpermissionmessage = ChatColor.RED + "権限がありません";

    public RyuZUCommandsGenerator(JavaPlugin plugin) {
        RyuZUCommandsGenerator.plugin = plugin;
        implement();
    }

    public RyuZUCommandsGenerator(JavaPlugin plugin , String resistpermissionmessage) {
        RyuZUCommandsGenerator.plugin = plugin;
        RyuZUCommandsGenerator.resistpermissionmessage = resistpermissionmessage;
        implement();
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    private static void implement() {
        for(String label : CommandsGenerator.commands.stream().map(CommandComposition::getLabel).collect(Collectors.toList())) {
            Objects.requireNonNull(getPlugin().getCommand(label)).setExecutor(executer);
            Objects.requireNonNull(getPlugin().getCommand(label)).setTabCompleter(executer);
        }
    }
}
