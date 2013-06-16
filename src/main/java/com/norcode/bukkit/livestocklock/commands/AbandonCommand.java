package com.norcode.bukkit.livestocklock.commands;

import com.norcode.bukkit.livestocklock.LivestockLock;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.LinkedList;
import java.util.List;

public class AbandonCommand extends BaseCommand {

    public AbandonCommand(LivestockLock plugin) {
        super(plugin, "abandon");
    }

    @Override
    public boolean onCommand(CommandSender sender, String label, LinkedList<String> args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command must be run by a player.");
            return true;
        }
        // remove a pending claim selection if there is one
        ((Player) sender).removeMetadata("livestocklock-claim-pending", plugin);
        ((Player) sender).setMetadata("livestocklock-abandon-pending", new FixedMetadataValue(plugin, true));
        sender.sendMessage("Now punch an animal to abandon it.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String label, LinkedList<String> args) {
        return null;
    }
}
