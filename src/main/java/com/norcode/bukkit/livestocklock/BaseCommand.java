package com.norcode.bukkit.livestocklock;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class BaseCommand implements TabExecutor {

    protected LivestockLock plugin;

    public BaseCommand(LivestockLock plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        LinkedList<String> params = new LinkedList<String>(Arrays.asList(args));
        return onCommand(commandSender, label, params);
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        LinkedList<String> params = new LinkedList<String>(Arrays.asList(args));
        return onTabComplete(commandSender, label, params);
    }

    public abstract boolean onCommand(CommandSender sender, String label, LinkedList<String> args);
    public abstract List<String> onTabComplete(CommandSender sender, String label, LinkedList<String> args);
}
