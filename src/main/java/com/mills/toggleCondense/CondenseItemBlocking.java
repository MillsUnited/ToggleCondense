package com.mills.toggleCondense;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CondenseItemBlocking implements Listener {

    private String preifx = ChatColor.translateAlternateColorCodes('&', "&6&lCondense &8» &7");

    @EventHandler
    public void onItemConsume(PlayerItemConsumeEvent e) {
        if (isCondensedItem(e.getItem())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(preifx + "You can't consume a condensed item!");
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (isCondensedItem(e.getItemInHand())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(preifx + "You can't place a condensed item!");
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getItem() != null && isCondensedItem(e.getItem())) {
            if (e.getAction().toString().contains("RIGHT") || e.getAction().toString().contains("LEFT")) {
                e.setCancelled(true);
            }
        }
    }

    private boolean isCondensedItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;

        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore()) return false;

        return meta.getLore().contains(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Forged by the fragments of InfernalMC");
    }

}
