package com.norcode.bukkit.livestocklock;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OwnedAnimal {

    private LivestockLock plugin;
    private UUID entityId;
    private String ownerName;

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

    public boolean allowAccess(Player player) {
        return player.getName().equals(ownerName) || plugin.getAccessList(ownerName).contains(player.getName());
    }
}
