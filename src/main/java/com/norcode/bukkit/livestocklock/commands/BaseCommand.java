package com.norcode.bukkit.livestocklock.commands;

import com.norcode.bukkit.livestocklock.LivestockLock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class BaseCommand implements TabExecutor {

    protected LivestockLock plugin;
    protected String name;
    protected String[] usage;

    public BaseCommand(LivestockLock plugin, String name, String[] usage) {
        this.plugin = plugin;
        this.name = name;
        this.usage = usage;
        this.plugin.registerSubcommand(name, this);
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

    public String getName() {
        return name;
    }

    public String[] getUsage() {
        return usage;
    }
}
