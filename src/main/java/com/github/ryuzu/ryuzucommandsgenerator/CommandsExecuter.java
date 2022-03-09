package com.github.ryuzu.ryuzucommandsgenerator;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandsExecuter implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        CommandData data = new CommandData(sender, command, label, args, false);
        for (CommandComposition constitution : CommandsGenerator.commands) {
            if(!constitution.getLabel().equals(command.getName())) break;
            if (!constitution.canExecute(data)) break;
            constitution.process(data);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> commands = new ArrayList<>();
        CommandData data = new CommandData(sender, command, command.getName(), args, true);
        for (CommandComposition constitution : CommandsGenerator.commands) {
            if(!constitution.getLabel().equals(command.getName())) break;
            List<String> complete = constitution.getTabComplete(data);
            if (complete == null) break;
            if (!constitution.canExecute(data)) break;
            commands.addAll(complete);
        }
        return commands;
    }
}
