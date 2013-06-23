package com.norcode.bukkit.livestocklock;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class ClaimableAnimal {

    LivestockLock plugin;
    short entityTypeId;
    private int costXP = 0;
    private double costMoney = 0.0;
    private ItemStack costItem = null;

    public ClaimableAnimal(LivestockLock plugin, short entityTypeId) {
        this.plugin = plugin;
        this.entityTypeId = entityTypeId;
        setupPermission();
    }

    private void setupPermission() {
        Permission perm = plugin.getServer().getPluginManager().getPermission("livestocklock.claim." + entityTypeId);
        if (perm == null) {
            perm = new Permission("livestocklock.claim." + entityTypeId, PermissionDefault.OP);
            plugin.getServer().getPluginManager().addPermission(perm);
            perm.addParent(plugin.getWildcardPermission(), true);
        }
    }

    public int getCostXP() {
        return costXP;
    }

    public void setCostXP(int costXP) {
        this.costXP = costXP;
    }

    public double getCostMoney() {
        return costMoney;
    }

    public void setCostMoney(double costMoney) {
        this.costMoney = costMoney;
    }

    public ItemStack getCostItem() {
        return costItem;
    }

    public void setCostItem(ItemStack costItem) {
        this.costItem = costItem;
    }

    public String getCostDescription() {
        StringBuilder desc = new StringBuilder();
        int i=0;
        if (costXP > 0) {
            i++;
            desc.append(costXP);
            desc.append(" XP, ");
        }
        if (costMoney > 0 && plugin.getEconomy() != null) {
            i++;
            desc.append(plugin.getEconomy().format(costMoney));
            desc.append(", ");
        }
        if (costItem != null) {
            i++;
            desc.append(costItem.getAmount());
            desc.append("x ");
            if (costItem.hasItemMeta() && costItem.getItemMeta().getDisplayName() != null) {
                desc.append(costItem.getItemMeta().getDisplayName());
            } else {
                desc.append(costItem.getType().name());
            }
        }
        String description = desc.toString();
        if (description.endsWith(", ")) {
            description = description.substring(0,description.length()-2);
        }
        if (i >= 2) {
            int lastCommaPos = description.lastIndexOf(',');
            description = description.substring(0,lastCommaPos) + " and " + description.substring(lastCommaPos + 2, description.length());
        }
        return description;
    }

    public boolean takeCost(Player player) {
        // check them all first.
        if (costXP > 0 && player.getExp() < costXP) {
            return false;
        }
        if (costMoney > 0 && plugin.getEconomy() != null && !plugin.getEconomy().has(player.getName(), costMoney)) {
            return false;
        }
        if (costItem != null && !player.getInventory().containsAtLeast(costItem, costItem.getAmount())) {
            return false;
        }
        // actually take it.
        if (costXP > 0) {
            player.setExp(player.getExp() - costXP);
        }
        if (costMoney > 0 && plugin.getEconomy() != null) {
            plugin.getEconomy().withdrawPlayer(player.getName(), costMoney);
        }
        if (costItem != null) {
            player.getInventory().removeItem(costItem);
        }
        return true;

    }
}
