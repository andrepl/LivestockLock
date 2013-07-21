package com.norcode.bukkit.livestocklock.commands;

import com.norcode.bukkit.livestocklock.LivestockLock;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.LinkedList;
import java.util.List;

public class HelpCommand extends BaseCommand {


    public HelpCommand(LivestockLock plugin) {
        super(plugin, "help", new String[] {
            "Detailed LivestockLock help.",
            "/<command> help - Help summary and command list.",
            "/<command> help <subcommand> - detailed usage for <subcommand>"
        });
    }

    @Override
    public boolean onCommand(CommandSender sender, String label, LinkedList<String> args) {
        LinkedList<String> lines = new LinkedList<String>();
        String cmdLbl = label.split(" ")[0];
        lines.add(ChatColor.GOLD + "" + ChatColor.BOLD + plugin.getName() + " v" + plugin.getDescription().getVersion() + ChatColor.RESET);
        lines.add("LivestockLock lets you protect your animals from being stolen, damaged, or otherwise messed with by other players.");
        lines.add("All commands are organized into subcommands accessed using /livestocklock or /lsl for short.  type `/lsl help <subcommand>` for detailed usage on any of the below subcommands.");
        if (args.size() == 0) {
            for (String scn: plugin.getSubcommands()) {
                BaseCommand cmd = plugin.getSubcommand(scn);
                lines.add(ChatColor.BOLD + scn + ChatColor.RESET + " " + cmd.getUsage()[0]);
            }
            sender.sendMessage(lines.toArray(new String[0]));
        } else {
            String subject = args.peek().toLowerCase();
            BaseCommand cmd = plugin.getSubcommand(subject);
            if (cmd == null) {
                sender.sendMessage("Unknown help topic: " + subject);
                return true;
            } else {
                sender.sendMessage(cmd.getUsage());
                return true;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String label, LinkedList<String> args) {
        return null;
    }
}
