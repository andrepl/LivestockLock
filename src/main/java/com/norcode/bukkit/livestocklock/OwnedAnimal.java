package com.norcode.bukkit.livestocklock;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.UUID;

public class OwnedAnimal {

    private LivestockLock plugin;
    private UUID entityId;
    private String ownerName;
    private EntityType entityType;

    public long getOwnerActivityTime() {
        return ownerActivityTime;
    }

    public void setOwnerActivityTime(long ownerActivityTime) {
        this.ownerActivityTime = ownerActivityTime;
    }

    private long ownerActivityTime;

    public OwnedAnimal(LivestockLock plugin, UUID entityId, String ownerName) {
        this.plugin = plugin;
        this.entityId = entityId;
        this.ownerName = ownerName;
    }


    /**
     * get the animal owner's name
     *
     * @return the name of the player who owns this animal
     */
    public String getOwnerName() {
        return ownerName;
    }

    void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    /**
     * check if the specified player is allowed to access this animal.
     *
     * @param player the player's name
     * @return true if they should be allowed access, false if not.
     */
    public boolean allowAccess(String player) {
        Player p = plugin.getServer().getPlayerExact(player);
        if (p != null && p.isOnline()) {
            if (p.hasMetadata("livestocklock-ignoring-claims")) {
                return true;
            }
        }
        return ownerName.equals(player) || plugin.getAccessList(ownerName).contains(player);
    }

    /**
     * get the entity's unique id
     *
     * @return UUID
     */
    public UUID getEntityId() {
        return entityId;
    }

    /**
     * get the entity's EntityType
     *
     * @return the entitys type
     */
    public EntityType getEntityType() {
        return entityType;
    }


    void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }
}
