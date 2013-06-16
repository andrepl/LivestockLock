package com.norcode.bukkit.livestocklock;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OwnedAnimal {
    private UUID entityId;
    private String ownerName;
    private List<String> accessList = new ArrayList<String>();

    public OwnedAnimal(UUID entityId, String ownerName) {
        this.entityId = entityId;
        this.ownerName = ownerName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public List<String> getAccessList() {
        return accessList;
    }

    public void setAccessList(List<String> accessList) {
        this.accessList = accessList;
    }

    public boolean allowAccess(Player player) {
        return player.getName().equals(ownerName) || accessList.contains(player.getName());
    }
}
