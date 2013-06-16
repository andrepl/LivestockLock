package com.norcode.bukkit.livestocklock.commands;

import com.norcode.bukkit.livestocklock.LivestockLock;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class AddPlayerCommand extends BaseCommand {

    public AddPlayerCommand(LivestockLock plugin) {
        super(plugin, "addplayer");
    }

    @Override
    public boolean onCommand(CommandSender sender, String label, LinkedList<String> args) {
        if (args.size() == 0) {
            return false;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command must be run by a player.");
            return true;
        }
        List<Player> matches = plugin.getServer().matchPlayer(args.peek());
        if (matches.size() != 1) {
            sender.sendMessage("Unknown Player: " + args.peek());
            return true;
        }
        List<String> accessList = plugin.getAccessList(sender.getName());
        if (accessList.contains(matches.get(0).getName())) {
            sender.sendMessage(matches.get(0).getName() + " is already trusted with your animals.");
            return true;
        }
        accessList.add(matches.get(0).getName());
        sender.sendMessage(matches.get(0).getName() + " is now trusted with your animals.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String label, LinkedList<String> args) {
        return null;
    }
}
