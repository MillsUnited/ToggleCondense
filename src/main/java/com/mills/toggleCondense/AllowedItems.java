package com.mills.toggleCondense;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AllowedItems {

    private final File file;
    private FileConfiguration config;

    public AllowedItems(File dataFolder) {

        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        file = new File(dataFolder, "allowed-items.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    private void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reindexItems() {
        ConfigurationSection section = config.getConfigurationSection("Allowed-Items");
        if (section == null) {
            section = config.createSection("Allowed-Items");
        }

        List<ItemStack> allItems = new ArrayList<>();

        for (String key : section.getKeys(false)) {
            ItemStack item = config.getItemStack("Allowed-Items." + key);
            if (item != null) allItems.add(item);
        }

        config.set("Allowed-Items", null);

        for (int i = 0; i < allItems.size(); i++) {
            config.set("Allowed-Items." + i, allItems.get(i));
        }

        saveConfig();
    }

    public void addItem(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return;

        ConfigurationSection section = config.getConfigurationSection("Allowed-Items");
        if (section == null) {
            section = config.createSection("Allowed-Items");
        }

        if (isItem(item)) return;

        int index = 0;
        while (section.contains(String.valueOf(index))) {
            index++;
        }

        config.set("Allowed-Items." + index, item);
        reindexItems();
        saveConfig();
    }

    public void removeItem(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return;

        ConfigurationSection section = config.getConfigurationSection("Allowed-Items");
        if (section == null) {
            section = config.createSection("Allowed-Items");
        }

        for (String key : section.getKeys(false)) {
            ItemStack stored = config.getItemStack("Allowed-Items." + key);
            if (stored != null && stored.isSimilar(item)) {
                config.set("Allowed-Items." + key, null);
                break;
            }
        }

        reindexItems();
        saveConfig();
    }

    public boolean isItem(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;

        ConfigurationSection section = config.getConfigurationSection("Allowed-Items");
        if (section == null) {
            section = config.createSection("Allowed-Items");
        }

        for (String key : section.getKeys(false)) {
            ItemStack stored = config.getItemStack("Allowed-Items." + key);
            if (stored != null && stored.isSimilar(item)) {
                return true;
            }
        }
        return false;
    }
}
