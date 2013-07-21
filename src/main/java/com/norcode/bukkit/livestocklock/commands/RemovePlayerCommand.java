package com.norcode.bukkit.livestocklock.commands;

import com.norcode.bukkit.livestocklock.LivestockLock;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class RemovePlayerCommand extends BaseCommand {

    public RemovePlayerCommand(LivestockLock plugin) {
        super(plugin, "removeplayer", new String[] {
            "Remove a player from your trust list.",
            "/<command> addplayer <player> - adds <player> to the list of players allowed to access your animals."
        });
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
        if (!accessList.contains(matches.get(0).getName())) {
            sender.sendMessage(matches.get(0).getName() + " is not trusted with your animals.");
            return true;
        }
        accessList.remove(matches.get(0).getName());
        sender.sendMessage(matches.get(0).getName() + " is no longer trusted with your animals.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String label, LinkedList<String> args) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
