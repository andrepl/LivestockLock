package com.norcode.bukkit.livestocklock.commands;

import com.norcode.bukkit.livestocklock.LivestockLock;
import com.norcode.bukkit.livestocklock.OwnedAnimal;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.LinkedList;
import java.util.List;

public class AbandonCommand extends BaseCommand {

    public AbandonCommand(LivestockLock plugin) {
        super(plugin, "abandon", new String[] {
            "Abandon one or all of your claimed animals.",
            "/<command> abandon - prompts you to click an animal you wish to abandon.",
            "/<command> abandon all - abandons all of your claimed animals.",
            "/<command> abandon all <player> - abandons all of <player>'s animals. (requires livestocklock.claimforothers permission)"
        });
    }

    @Override
    public boolean onCommand(CommandSender sender, String label, LinkedList<String> args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command must be run by a player.");
            return true;
        }
        // remove a pending claim selection if there is one
        ((Player) sender).removeMetadata("livestocklock-claim-pending", plugin);
        if (args.size() >= 1 && args.peek().toLowerCase().equals("all")) {
            args.pop();
            Player targetPlayer = (Player) sender;
            if (args.size() == 1) {
                if (!sender.hasPermission("livestocklock.claimforothers")) {
                    sender.sendMessage("You don't have permission to abandon other player's animals.");
                    return true;
                }
                List<Player> matches = plugin.getServer().matchPlayer(args.peek());
                if (matches.size() != 1) {
                    sender.sendMessage("Unknown player: " + args.peek());
                    return true;
                }
                targetPlayer = matches.get(0);
            }
            for (OwnedAnimal oa: plugin.getOwnedAnimals(targetPlayer.getName())) {
                plugin.removeOwnedAnimal(oa);
            }
            sender.sendMessage("All of " + (targetPlayer.equals(sender) ? "your" : targetPlayer.getName() + "'s") + " animals have been abandoned.");
            return true;
        } else {
            ((Player) sender).setMetadata("livestocklock-abandon-pending", new FixedMetadataValue(plugin, true));
            sender.sendMessage("Now punch an animal to abandon it.");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String label, LinkedList<String> args) {
        return null;
    }
}
