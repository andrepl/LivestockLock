package com.norcode.bukkit.livestocklock;

public class DataStore {

    private LivestockLock plugin;

    public DataStore(LivestockLock plugin) {
        this.plugin = plugin;
    }

    public void onEnable() {
        // Load all data.
    }

    public void onDisable() {
        // save all data.
    }
}
