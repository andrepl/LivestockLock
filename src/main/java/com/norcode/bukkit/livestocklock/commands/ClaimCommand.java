package com.norcode.bukkit.livestocklock.commands;

import com.norcode.bukkit.livestocklock.LivestockLock;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class ClaimCommand extends BaseCommand {

    public ClaimCommand(LivestockLock plugin) {
        super(plugin, "claim");
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

        List<UUID> alreadyOwned = plugin.getOwnedAnimals(owner.getName());
        if (alreadyOwned.size() >= plugin.getPlayerClaimLimit(owner)) {
            sender.sendMessage((owner == sender ? "You aren't" : owner.getName() + " isn't") + " allowed to own any more animals.");
            return true;
        }
        // remove a pending abandonment selection if there is one
        ((Player) sender).removeMetadata("livestocklock-abandon-pending", plugin);
        ((Player) sender).setMetadata("livestocklock-claim-pending", new FixedMetadataValue(plugin, owner));
        sender.sendMessage("Now go punch an animal to claim it.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String lbl, LinkedList<String> args) {
        return null;
    }
}
