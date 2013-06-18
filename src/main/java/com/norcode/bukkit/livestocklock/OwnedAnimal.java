package com.norcode.bukkit.livestocklock;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OwnedAnimal {

    private LivestockLock plugin;
    private UUID entityId;
    private String ownerName;
    private EntityType entityType;

    public OwnedAnimal(LivestockLock plugin, UUID entityId, String ownerName) {
        this.plugin = plugin;
        this.entityId = entityId;
        this.ownerName = ownerName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public boolean allowAccess(String player) {
        Player p = plugin.getServer().getPlayerExact(player);
        if (p != null && p.isOnline()) {
            if (p.hasMetadata("livestocklock-ignoring-claims")) {
                return true;
            }
        }
        return ownerName.equals(player) || plugin.getAccessList(ownerName).contains(player);
    }

    public UUID getEntityId() {
        return entityId;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }
}
