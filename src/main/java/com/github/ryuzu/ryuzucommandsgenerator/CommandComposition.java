package com.github.ryuzu.ryuzucommandsgenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CommandComposition {
    private final String command;
    private final Consumer<CommandData> process;
    private List<String> permissions = new ArrayList<>();
    private Predicate<CommandData> condition;
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
        return (String[]) Arrays.stream(command.split("\\.")).filter(arg -> !arg.equals(getLabel())).toArray();
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

    public Consumer<CommandData> getProcess() {
        return process;
    }

    public boolean hasPermission(CommandData data) {
        boolean has = permissions.stream().anyMatch(perm -> data.getSender().hasPermission(perm));
        if(!has) data.getSender().sendMessage(RyuZUCommandsGenerator.resistpermissionmessage);
        return has;
    }

    public boolean canExecute(CommandData data) {
        return data.getArgs().length >= getArgs().length && hasPermission(data) && condition.test(data);
    }

    public void process(CommandData data) {
        process.accept(data);
    }

    public List<String> getTabComplete(CommandData data) {
        String[] commandargs = getArgs();
        int length = Math.min(data.getArgs().length , commandargs.length);
        if(length == 0) return null;
        for (int i = 0; i < length; i++) {
            if (length - 1 == i) {
                if (!commandargs[i].startsWith(data.getArgs()[i])) {
                    return null;
                }
            } else {
                if(commandargs[i].equalsIgnoreCase("#object#")) break;
                if (!commandargs[i].equals(data.getArgs()[i])) {
                    return null;
                }
            }
        }
        if(completer.getCompletes().size() < data.getArgs().length) return null;
        return completer.getCompletes().get(data.getArgs().length - 1).apply(data);

    }
}
