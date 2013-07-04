package com.norcode.bukkit.livestocklock;

import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class EntityListener implements Listener {

    LivestockLock plugin;

    public EntityListener(LivestockLock plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled=true)
    public void onEntityDeath(EntityDeathEvent event) {
        OwnedAnimal oa = plugin.getOwnedAnimal(event.getEntity().getUniqueId());
        if (oa != null) {
            plugin.removeOwnedAnimal(oa);
        }
    }



    @EventHandler(ignoreCancelled=true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        OwnedAnimal oa = plugin.getOwnedAnimal(event.getEntity().getUniqueId());
        if (oa == null) return;
        Player damager = null;
        if (event.getDamager().getType() == EntityType.PLAYER) {
            damager = (Player) event.getDamager();
        } else if (event.getEntity() instanceof Projectile) {
            Projectile p = (Projectile) event.getDamager();
            if (p.getShooter() instanceof Player) {
                damager = (Player) p.getShooter();
            }
        }
        if (damager != null && !oa.allowAccess(damager.getName())) {
            event.setCancelled(true);
            damager.sendMessage("That animal belongs to " + oa.getOwnerName());
            return;
        }
    }

    @EventHandler(ignoreCancelled=true)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity animal = event.getRightClicked();
        if (plugin.getOwnedAnimals().containsKey(animal.getUniqueId())) {
            // This animal is owned, check for permission.
            OwnedAnimal oa = plugin.getOwnedAnimal(animal.getUniqueId());
            if (player.hasMetadata("livestocklock-abandon-pending")) {
                player.removeMetadata("livestocklock-abandon-pending", plugin);
                if (oa.getOwnerName().equals(player.getName()) || player.hasPermission("livestocklock.claimforothers")) {
                    player.sendMessage("This animal has been abandoned.");
                    plugin.removeOwnedAnimal(oa);
                    return;
                } else {
                    player.sendMessage("This animal doesn't belong to you!");
                    return;
                }
            } else if (!oa.allowAccess(player.getName())) {
                player.sendMessage("Sorry, that animal belongs to " + oa.getOwnerName());
                Player owner = plugin.getServer().getPlayerExact(oa.getOwnerName());
                if (owner != null && owner.isOnline()) {
                    owner.sendMessage(player.getName() + " is trying to use your animal at " + formatLoc(player.getLocation()));
                }
                event.setCancelled(true);
            }
        } else if (player.hasMetadata("livestocklock-claim-pending")) {
            String ownerName = player.getMetadata("livestocklock-claim-pending").get(0).asString();
            player.removeMetadata("livestocklock-claim-pending", plugin);
            plugin.getLogger().info("Attempting to claim: " + animal.getType() + ", " + animal.getType().getTypeId());
            if (plugin.getClaimableAnimals().containsKey(animal.getType().getTypeId())) {
                if (player.hasPermission("livestocklock.claim." + animal.getType().getTypeId())) {
                    if (animal instanceof Tameable && !((Tameable) animal).isTamed()) {
                        if (plugin.getConfig().getBoolean("require-taming", true)) {
                            player.sendMessage("You can't claim a wild animal.");
                            event.setCancelled(true);
                            return;
                        }
                    } else if ((animal instanceof Ageable) && !((Ageable) animal).isAdult()) {
                        if (plugin.getConfig().getBoolean("require-adulthood", true)) {
                            player.sendMessage("You can't claim a baby animal.");
                            event.setCancelled(true);
                            return;
                        }
                    }
                    ClaimableAnimal ca = plugin.getClaimableAnimals().get(animal.getType().getTypeId());
                    event.setCancelled(true);
                    if (ca.takeCost(player)) {
                        OwnedAnimal oa = new OwnedAnimal(plugin, animal.getUniqueId(), ownerName);
                        oa.setEntityType(animal.getType());
                        plugin.saveOwnedAnimal(oa);
                        player.sendMessage("This " + oa.getEntityType().name() + " now belongs to you.");
                    } else {
                        player.sendMessage("Sorry, you don't have " + ca.getCostDescription());
                    }
                }
            }
        }
    }

    public static String formatLoc(Location l) {
        return l.getWorld().getName() + ": " + l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ();
    }
}
