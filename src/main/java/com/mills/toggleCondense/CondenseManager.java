package com.mills.toggleCondense;

import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class CondenseManager {

    public String preifx = ChatColor.translateAlternateColorCodes('&', "&6&lCondense &8» &7");

    private final Set<UUID> condenseToggled = new HashSet<>();
    private Map<UUID, Map<ItemStack, Integer>> collectedItems = new HashMap<>();

    public void toggleCondense(Player player) {
        UUID uuid = player.getUniqueId();
        if (condenseToggled.contains(uuid)) {
            condenseToggled.remove(uuid);
            refundUncondensedItems(player);
            player.sendMessage(preifx + ChatColor.translateAlternateColorCodes('&', "&cdisabled &7toggle condense!"));
        } else {
            condenseToggled.add(uuid);
            collectedItems.putIfAbsent(uuid, new HashMap<>());
            player.sendMessage(preifx + ChatColor.translateAlternateColorCodes('&', "&aenabled &7toggle condense!"));
        }
    }

    public boolean isCondenseEnabled(Player player) {
        return condenseToggled.contains(player.getUniqueId());
    }

    public void addItem(Player player, ItemStack item, int amount) {
        UUID uuid = player.getUniqueId();
        if (!isCondenseEnabled(player)) return;

        Map<ItemStack, Integer> playerItems = collectedItems.get(uuid);
        ItemStack key = normalizeItem(item);

        int current = playerItems.getOrDefault(key, 0);
        int total = current + amount;

        int condensedCount = total / 2000;
        int remainder = total % 2000;

        if (condensedCount > 0) {
            ItemStack condensed = createCondensedItem(key, condensedCount);
            player.getInventory().addItem(condensed);
        }

        playerItems.put(key, remainder);
    }

    private ItemStack createCondensedItem(ItemStack base, int amount) {
        ItemStack item = new ItemStack(base.getType());
        item.setAmount(amount);

        ItemMeta meta = item.getItemMeta();;
        String displayName = format(meta != null && meta.hasDisplayName()
                ? meta.getDisplayName()
                : base.getType().name());
        meta.setDisplayName(ChatColor.WHITE + ChatColor.BOLD.toString() + "Condensed " + displayName);

        meta.addEnchant(Enchantment.UNBREAKING, 1, true);
        meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);

        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Forged by the fragments of InfernalMC");
        meta.setLore(lore);

        item.setItemMeta(meta);

        return item;
    }

    public Integer getItemAmount(Player player, ItemStack item) {
        UUID uuid = player.getUniqueId();
        Map<ItemStack, Integer> playerItems = collectedItems.get(uuid);
        if (playerItems == null) return 0;

        ItemStack key = normalizeItem(item);

        int amount = playerItems.getOrDefault(key, 0);
        return amount;
    }

    private void refundUncondensedItems(Player player) {
        UUID uuid = player.getUniqueId();
        Map<ItemStack, Integer> playerItems = collectedItems.remove(uuid);
        if (playerItems == null) return;

        for (Map.Entry<ItemStack, Integer> entry : playerItems.entrySet()) {
            int amount = entry.getValue();
            if (amount <= 0) continue;

            ItemStack item = entry.getKey().clone();
            item.setAmount(amount);

            HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(item);

            for (ItemStack leftover : leftovers.values()) {
                player.getWorld().dropItemNaturally(player.getLocation(), leftover);
            }
        }
    }

    private ItemStack normalizeItem(ItemStack item) {
        ItemStack key = new ItemStack(item.getType());

        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            ItemMeta original = item.getItemMeta();
            ItemMeta meta = key.getItemMeta();
            meta.setDisplayName(original.getDisplayName());
            key.setItemMeta(meta);
        }

        return key;
    }

    private String format(String string) {
        if (string == null || string.isEmpty()) return "";

        StringBuilder builder = new StringBuilder();

        for (String word : string.split("_")) {
            if (word.isEmpty()) continue;
            String first = word.substring(0, 1).toUpperCase();
            String second = word.length() > 1 ? word.substring(1).toLowerCase() : "";
            builder.append(first).append(second).append(" ");
        }

        return builder.toString().trim();
    }


}
