package com.github.ryuzu.ryuzucommandsgenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CommandComposition {
    private final String command;
    private final Consumer<CommandData> process;
    private List<String> permissions = new ArrayList<>();
    private Predicate<CommandData> condition = (data) -> true;
    private Predicate<CommandData> tabcompleteconditon = (data) -> true;
    private TabCompleter completer;

    public CommandComposition(String command, Consumer<CommandData> process) {
        this.command = command;
        this.process = process;
        this.completer = new TabCompleter(command);
    }

    String getLabel() {
        return command.split("\\.")[0];
    }

    public String[] getArgs() {
        return Arrays.stream(command.split("\\.")).filter(arg -> !arg.equals(getLabel())).toArray(String[]::new);
    }

    void setPermissions(List<String> permissions) {
        this.permissions = new ArrayList<>(permissions);
    }

    void setCondition(Predicate<CommandData> condition) {
        this.condition = condition;
    }

    Predicate<CommandData> getTabcompleteconditon() {
        return tabcompleteconditon;
    }

    void setTabcompleteConditon(Predicate<CommandData> tabcompleteconditon) {
        this.tabcompleteconditon = tabcompleteconditon;
    }

    boolean hasPermission(CommandData data) {
        boolean has = permissions.stream().anyMatch(perm -> data.getSender().hasPermission(perm));
        if (!has) data.sendMessage(RyuZUCommandsGenerator.resistpermissionmessage);
        return has;
    }

    boolean canTabComplete(CommandData data) {
        return data.getArgs().length >= getArgs().length && getTabComplete(data) != null && tabcompleteconditon.test(data) && hasPermission(data);
    }

    boolean canExecute(CommandData data) {
        if (!canTabComplete(data)) return false;
        return condition.test(data);
    }

    void process(CommandData data) {
        process.accept(data);
    }

    List<String> getTabComplete(CommandData data) {
        int length = Math.min(data.getArgs().length, getArgs().length);
        if (length == 0) return new ArrayList<>();
        for (int i = 0; i < length; i++) {
            if (length - 1 == i) {
                if (data.isTabComplete())
                    if (!getArgs()[i].startsWith(data.getArgs()[i])) return null;
                else
                    if (!getArgs()[i].equals(data.getArgs()[i])) return null;
            } else {
                if (getArgs()[i].equalsIgnoreCase("#object#")) continue;
                if (!getArgs()[i].equals(data.getArgs()[i])) return null;
            }
        }
        if (completer.getCompletes().size() > data.getArgs().length) return null;
        if (completer.getCompletes().size() < data.getArgs().length) return new ArrayList<>();
        return completer.getCompletes().get(data.getArgs().length - 1).stream()
                .map(complete -> complete.apply(data))
                .flatMap(List::stream)
                .filter(complete -> complete.startsWith(data.getArgs()[data.getArgs().length - 1]))
                .collect(Collectors.toList());
    }

    boolean getLastMacth(CommandData data) {
        int length = Math.min(data.getArgs().length, getArgs().length);
        if (length == 0) return true;
        return getArgs()[length - 1].equals(data.getArgs()[length - 1]);
    }

    public CommandComposition permissions(String... permissions) {
        setPermissions(Arrays.asList(permissions));
        return this;
    }

    public CommandComposition permissions(List<String> permissions) {
        setPermissions(permissions);
        return this;
    }

    public CommandComposition condition(Predicate<CommandData> condition) {
        setCondition(condition);
        return this;
    }

    public CommandComposition tabCompleteConditon(Predicate<CommandData> tabcompleteconditon) {
        setTabcompleteConditon(tabcompleteconditon);
        return this;
    }

    public CommandComposition complete(int index, List<String> complete) {
        completer.addComplete(index, data -> complete);
        return this;
    }

    public CommandComposition complete(int index, Function<CommandData, List<String>> complete) {
        completer.addComplete(index, complete);
        return this;
    }

    public CommandComposition onlinePlayerComplete(int index) {
        completer.setOnlinePlayerComplete(index);
        return this;
    }
}
