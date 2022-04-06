package com.github.ryuzu.ryuzucommandsgenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CommandsGenerator {
    public static List<CommandComposition> commands = new ArrayList<>();

    public static void registerCommand(String command, Consumer<CommandData> process) {
        CommandComposition constitution = new CommandComposition(command, process);
        CommandsGenerator.commands.add(constitution);
    }

    public static void registerCommand(String command, Consumer<CommandData> process, List<String> permissions) {
        CommandComposition constitution = new CommandComposition(command, process);
        constitution.setPermissions(permissions);
        CommandsGenerator.commands.add(constitution);
    }

    public static void registerCommand(String command, Consumer<CommandData> process, String permission) {
        CommandComposition constitution = new CommandComposition(command, process);
        constitution.setPermissions(Collections.singletonList(permission));
        CommandsGenerator.commands.add(constitution);
    }

    public static void registerCommand(String command, Consumer<CommandData> process, List<String> permissions, Predicate<CommandData> condition) {
        CommandComposition constitution = new CommandComposition(command, process);
        constitution.setPermissions(permissions);
        constitution.setCondition(condition);
        CommandsGenerator.commands.add(constitution);
    }

    public static void registerCommand(String command, Consumer<CommandData> process, String permission, Predicate<CommandData> condition) {
        CommandComposition constitution = new CommandComposition(command, process);
        constitution.setPermissions(Collections.singletonList(permission));
        constitution.setCondition(condition);
        CommandsGenerator.commands.add(constitution);
    }

    public static void registerCommand(String command, Consumer<CommandData> process, List<String>  permissions, Predicate<CommandData> condition, Predicate<CommandData> tabcompletecondition) {
        CommandComposition constitution = new CommandComposition(command, process);
        constitution.setPermissions(permissions);
        constitution.setCondition(condition);
        constitution.setTabcompleteconditon(tabcompletecondition);
        CommandsGenerator.commands.add(constitution);
    }

    public static void registerCommand(String command, Consumer<CommandData> process, String permission, Predicate<CommandData> condition, Predicate<CommandData> tabcompletecondition) {
        CommandComposition constitution = new CommandComposition(command, process);
        constitution.setPermissions(Collections.singletonList(permission));
        constitution.setCondition(condition);
        constitution.setTabcompleteconditon(tabcompletecondition);
        CommandsGenerator.commands.add(constitution);
    }
}
