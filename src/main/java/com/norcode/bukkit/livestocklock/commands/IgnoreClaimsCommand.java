package com.norcode.bukkit.livestocklock.commands;

import com.norcode.bukkit.livestocklock.LivestockLock;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.LinkedList;
import java.util.List;

public class IgnoreClaimsCommand extends BaseCommand {
    public IgnoreClaimsCommand(LivestockLock plugin) {
        super(plugin, "ignoreclaims", new String[] {
            "/<command> ignoreclaims - toggles whether you're currently ignoring claim protections."
        });
    }

    @Override
    public boolean onCommand(CommandSender sender, String label, LinkedList<String> args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command must be run by a player.");
            return true;
        }
        Player player = (Player) sender;
        if (player.hasMetadata("livestocklock-ignoring-claims")) {
            sender.sendMessage("You are no longer ignoring animal claims.");
            player.removeMetadata("livestocklock-ignoring-claims", plugin);
            return true;
        }
        sender.sendMessage("You are now ignoring all animal claims, use your power wisely.");
        player.setMetadata("livestocklock-ignoring-claims", new FixedMetadataValue(plugin, true));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String label, LinkedList<String> args) {
        return null;
    }
}
