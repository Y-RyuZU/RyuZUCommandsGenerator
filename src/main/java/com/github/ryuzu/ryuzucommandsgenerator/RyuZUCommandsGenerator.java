package com.github.ryuzu.ryuzucommandsgenerator;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.stream.Collectors;

public class RyuZUCommandsGenerator {
    private static JavaPlugin plugin;
    public static CommandsExecuter executer = new CommandsExecuter();
    public static String resistPermissionMessage = ChatColor.RED + "権限がありません";

    private RyuZUCommandsGenerator(JavaPlugin plugin) {
        RyuZUCommandsGenerator.plugin = plugin;
        implement();
    }

    private RyuZUCommandsGenerator(JavaPlugin plugin , String resistPermissionMessage) {
        RyuZUCommandsGenerator.plugin = plugin;
        RyuZUCommandsGenerator.resistPermissionMessage = resistPermissionMessage;
        implement();
    }

    public static void initialize(JavaPlugin plugin) {
        RyuZUCommandsGenerator.plugin = plugin;
        implement();
    }

    public static void initialize(JavaPlugin plugin , String resistPermissionMessage) {
        RyuZUCommandsGenerator.plugin = plugin;
        RyuZUCommandsGenerator.resistPermissionMessage = resistPermissionMessage;
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
