package com.norcode.bukkit.livestocklock;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class ClaimCommand extends BaseCommand {

    public ClaimCommand(LivestockLock plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, String lbl, LinkedList<String> args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command must be run by a player.");
            return false;
        }

        Player owner = (Player) sender;
        if (args.size() > 0) {
            if (!sender.hasPermission("livestocklock.claimforothers")) {
                sender.sendMessage("You don't have permission to claim an animal for another player.");
                return true;
            }
            List<Player> matches = plugin.getServer().matchPlayer(args.peek());
            if (matches.size() != 1) {
                sender.sendMessage("Unknown player: " + args.peek());
                return true;
            }
            owner = matches.get(0);
        }


        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String lbl, LinkedList<String> args) {
        return null;
    }
}
