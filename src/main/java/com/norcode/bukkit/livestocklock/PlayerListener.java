package com.norcode.bukkit.livestocklock;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerListener implements Listener {

    LivestockLock plugin;

    public PlayerListener(LivestockLock plugin) {
        this.plugin = plugin;
    }

    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity animal = event.getRightClicked();
        if (plugin.getOwnedAnimals().containsKey(animal.getUniqueId())) {
            // This animal is owned, check for permission.
            OwnedAnimal oa = plugin.getOwnedAnimal(animal.getUniqueId());
            if (!oa.allowAccess(event.getPlayer())) {
                player.sendMessage("Sorry, that animal belongs to " + oa.getOwnerName());
                event.setCancelled(true);
            }
        } else if (plugin.getAllowedAnimals().containsKey(animal.getType().getTypeId())) {
            if (player.hasPermission("livestocklock.claim." + animal.getType().getTypeId())) {
                ClaimableAnimal ca = plugin.getAllowedAnimals().get(animal.getType().getTypeId());
                event.setCancelled(true);
                if (ca.takeCost(player)) {
                    OwnedAnimal oa = new OwnedAnimal(animal.getUniqueId(), player.getName());
                    plugin.getOwnedAnimals().put(animal.getUniqueId(), oa);
                } else {
                    player.sendMessage("Sorry, you don't have " + ca.getCostDescription());
                }
            }
        }
    }
}
