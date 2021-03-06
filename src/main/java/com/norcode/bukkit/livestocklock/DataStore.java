package com.norcode.bukkit.livestocklock;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DataStore {

    private LivestockLock plugin;
    private ConfigAccessor accessor;

    public DataStore(LivestockLock plugin) {
        this.plugin = plugin;
    }

    public void onEnable() {
        this.accessor = new ConfigAccessor(plugin, "data.yml");
        this.accessor.getConfig();
    }

    public void onDisable() {
        this.accessor.saveConfig();
    }

    public void saveAccessLists(HashMap<String, List<String>> accessLists) {
        accessor.getConfig().set("access-lists", null);
        ConfigurationSection accessSection = accessor.getConfig().createSection("access-lists");
        for (String playerName: accessLists.keySet()) {
            accessSection.set(playerName, accessLists.get(playerName));
        }
    }

    public void saveOwnedAnimals(Map<UUID, OwnedAnimal> ownedAnimals) {
        accessor.getConfig().set("owned-animals", null);
        ConfigurationSection animalSection = accessor.getConfig().createSection("owned-animals");
        for (UUID uuid: ownedAnimals.keySet()) {
            ConfigurationSection petSection = animalSection.createSection(uuid.toString());
            petSection.set("owner", ownedAnimals.get(uuid).getOwnerName());
            petSection.set("entity-type", ownedAnimals.get(uuid).getEntityType().name());
        }
    }

    public HashMap<String, List<String>> getAccessLists() {
        ConfigurationSection accessSection = accessor.getConfig().getConfigurationSection("access-lists");
        if (accessSection != null) {
            HashMap<String, List<String>> accessLists = new HashMap<String, List<String>>();
            for (String playerName: accessSection.getKeys(false)) {
                accessLists.put(playerName, accessSection.getStringList(playerName));
            }
            return accessLists;
        }
        return new HashMap<String, List<String>>();
    }

    public Map<UUID, OwnedAnimal> getOwnedAnimals() {
        long now = System.currentTimeMillis();
        ConfigurationSection animalSection = accessor.getConfig().getConfigurationSection("owned-animals");
        if (animalSection != null) {
            HashMap<UUID, OwnedAnimal> ownedAnimals = new HashMap<UUID, OwnedAnimal>();
            for (String uuid: animalSection.getKeys(false)) {
                OwnedAnimal oa = new OwnedAnimal(plugin, UUID.fromString(uuid), animalSection.getConfigurationSection(uuid).getString("owner"));
                oa.setEntityType(EntityType.valueOf(animalSection.getConfigurationSection(uuid).getString("entity-type")));
                oa.setOwnerActivityTime(animalSection.getConfigurationSection(uuid).getLong("owner-activity-time", now));
                ownedAnimals.put(UUID.fromString(uuid), oa);
            }
            return ownedAnimals;
        }
        return new HashMap<UUID, OwnedAnimal>();
    }
}
