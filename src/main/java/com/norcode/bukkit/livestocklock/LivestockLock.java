package com.norcode.bukkit.livestocklock;
import com.norcode.bukkit.livestocklock.OwnedAnimal;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;


public class LivestockLock extends JavaPlugin {

    private Map<UUID, OwnedAnimal> ownedAnimals = new HashMap<UUID, OwnedAnimal>();
    private HashMap<Short, ClaimableAnimal> allowedAnimals = new HashMap<Short, ClaimableAnimal>();
    private HashMap<String, List<String>> accessLists = new HashMap<String, List<String>>();

    private boolean debugMode = false;
    private Economy economy = null;
    private DataStore datastore;

    public void onEnable() {
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        initializeVault();
        saveConfig();
        loadConfig();
        initializeDatastore();
    }

    public void onDisable() {
        if (datastore != null) {
            datastore.onDisable();
        }
    }

    private void initializeVault() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        } else {
            getLogger().warning("No appropriate economy plugin found.  Economy costs will not function.");
        }
    }

    private void initializeDatastore() {
        this.datastore = new DataStore(this);
        this.datastore.onEnable();
    }

    public void loadConfig() {
        // Load set of allowed entities
        ConfigurationSection animalSection = getConfig().getConfigurationSection("allowed-entity-types");
        allowedAnimals.clear();
        ClaimableAnimal ca;
        ConfigurationSection sect;
        short eid;
        for (String s: animalSection.getKeys(false)) {

            sect = animalSection.getConfigurationSection(s);
            String eType = sect.getString("entity-type-id", "");
            eid = -1;
            try {
                eid = Short.parseShort(s);
            } catch (IllegalArgumentException ex) {
                EntityType et = EntityType.valueOf(s.toUpperCase());
                if (et == null) {
                    getLogger().warning("Unknown EntityType: " + s);
                    continue;
                }
                eid = et.getTypeId();
            }
            ca = new ClaimableAnimal(this, (short) eid);
            ca.setCostMoney(sect.getDouble("cost-money"));
            ca.setCostXP(sect.getInt("cost-xp"));
            ca.setCostItem(sect.getItemStack("cost-item"));
            allowedAnimals.put(eid, ca);
        }

        debugMode = getConfig().getBoolean("debug");
    }

    public Map<UUID, OwnedAnimal> getOwnedAnimals() {
        return ownedAnimals;
    }

    public OwnedAnimal getOwnedAnimal(UUID uniqueId) {
        return ownedAnimals.get(uniqueId);
    }

    public Map<Short, ClaimableAnimal> getAllowedAnimals() {
        return allowedAnimals;
    }

    public Economy getEconomy() {
        return economy;
    }

    public List<String> getAccessList(String ownerName) {
        if (!accessLists.containsKey(ownerName)) {
            return new ArrayList<String>(0);
        }
        return accessLists.get(ownerName);
    }

}
