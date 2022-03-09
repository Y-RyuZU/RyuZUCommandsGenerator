package com.github.ryuzu.ryuzucommandsgenerator;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TabCompleter {
    private final List<Function<CommandData, List<String>>> completes;

    public TabCompleter(List<Function<CommandData, List<String>>> completes) {
        this.completes = completes;
    }

    public TabCompleter(String command) {
        this.completes = new ArrayList<>();
        String[] args = command.split("\\.");
        for (String arg : args) {
            this.completes.add(data -> Collections.singletonList(arg));
        }
        this.completes.remove(0);
    }

    public List<Function<CommandData, List<String>>> getCompletes() {
        return completes;
    }

    public void setComplete(int index, Function<CommandData, List<String>> complete) {
        this.completes.set(index, complete);
    }

    public void setOnlinePlayerComplete(int index) {
        if (this.completes.size() > index)
            this.completes.set(index, data -> Bukkit.getServer().getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()));
        else
            this.completes.add(data -> Bukkit.getServer().getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()));
    }
}
