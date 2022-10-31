package com.github.ryuzu.ryuzucommandsgenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
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

    public String getLabel() {
        return command.split("\\.")[0];
    }

    public String getCommand() {
        return command;
    }

    public String[] getArgs() {
        return Arrays.stream(command.split("\\.")).filter(arg -> !arg.equals(getLabel())).toArray(String[]::new);
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public Predicate<CommandData> getCondition() {
        return condition;
    }

    public void setCondition(Predicate<CommandData> condition) {
        this.condition = condition;
    }

    public Predicate<CommandData> getTabcompleteconditon() {
        return tabcompleteconditon;
    }

    public void setTabcompleteconditon(Predicate<CommandData> tabcompleteconditon) {
        this.tabcompleteconditon = tabcompleteconditon;
    }

    public Consumer<CommandData> getProcess() {
        return process;
    }

    public boolean hasPermission(CommandData data) {
        boolean has = permissions.stream().anyMatch(perm -> data.getSender().hasPermission(perm));
        if (!has) data.sendMessage(RyuZUCommandsGenerator.resistpermissionmessage);
        return has;
    }

    public boolean canTabComplete(CommandData data) {
        return data.getArgs().length >= getArgs().length && getTabComplete(data) != null && hasPermission(data) && tabcompleteconditon.test(data);
    }

    public boolean canExecute(CommandData data) {
        if (!canTabComplete(data)) return false;
        return condition.test(data);
    }

    public void process(CommandData data) {
        process.accept(data);
    }

    public List<String> getTabComplete(CommandData data) {
        String[] commandargs = getArgs();
        int length = Math.min(data.getArgs().length, commandargs.length);
        if (length == 0) return new ArrayList<>();
        for (int i = 0; i < length; i++) {
            if (length - 1 == i) {
                if(data.isTabComplete()) if (!commandargs[i].startsWith(data.getArgs()[i])) return null;
                else if (!commandargs[i].equals(data.getArgs()[i])) return null;
            } else {
                if (commandargs[i].equalsIgnoreCase("#object#")) continue;
                if (!commandargs[i].equals(data.getArgs()[i])) return null;
            }
        }
        if (completer.getCompletes().size() > data.getArgs().length) return null;
        if (completer.getCompletes().size() < data.getArgs().length) return new ArrayList<>();
        return completer.getCompletes().get(data.getArgs().length - 1).apply(data);
    }

    public boolean getLastMacth(CommandData data) {
        int length = Math.min(data.getArgs().length, getArgs().length);
        if (length == 0) return true;
        return getArgs()[length - 1].equals(data.getArgs()[length - 1]);
    }
}
