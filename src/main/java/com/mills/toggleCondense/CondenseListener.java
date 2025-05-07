package com.mills.toggleCondense;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CondenseListener implements Listener {

    private CondenseManager condenseManager;

    public CondenseListener(CondenseManager condenseManager) {
        this.condenseManager = condenseManager;
    }

    private String prefix = ChatColor.translateAlternateColorCodes('&', "&6&lCondense &8» &a");

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (player.getGameMode() != GameMode.SURVIVAL) return;
        if (!condenseManager.isCondenseEnabled(player)) return;

        e.setDropItems(false);
        Block block = e.getBlock();
        Collection<ItemStack> drops = block.getDrops(player.getInventory().getItemInMainHand());

        Map<ItemStack, Integer> totalDrops = new HashMap<>();

        for (ItemStack drop : drops) {
            if (drop == null || drop.getType().isAir()) continue;

            boolean matched = false;
            for (ItemStack key : totalDrops.keySet()) {
                if (key.isSimilar(drop)) {
                    totalDrops.put(key, totalDrops.get(key) + drop.getAmount());
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                totalDrops.put(drop.clone(), drop.getAmount());
            }
        }

        for (Map.Entry<ItemStack, Integer> entry : totalDrops.entrySet()) {
            ItemStack item = entry.getKey();
            int totalAmount = entry.getValue();

            condenseManager.addItem(player, item, totalAmount);
            int current = condenseManager.getItemAmount(player, item);

            player.sendActionBar(prefix + "+" + totalAmount + ChatColor.GRAY
                    + " (" + ChatColor.GREEN + current + ChatColor.GRAY + " / " + ChatColor.GREEN + "2,000" + ChatColor.GRAY + " )");
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        LivingEntity entity = e.getEntity();
        Player player = entity.getKiller();

        if (entity instanceof Player) return;
        if (player == null) return;
        if (player.getGameMode() != GameMode.SURVIVAL) return;
        if (!condenseManager.isCondenseEnabled(player)) return;

        List<ItemStack> drops = new ArrayList<>(e.getDrops());
        e.getDrops().clear();

        Map<ItemStack, Integer> totalDrops = new HashMap<>();

        for (ItemStack drop : drops) {
            if (drop == null || drop.getType().isAir()) continue;

            boolean matched = false;
            for (ItemStack key : totalDrops.keySet()) {
                if (key.isSimilar(drop)) {
                    totalDrops.put(key, totalDrops.get(key) + drop.getAmount());
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                totalDrops.put(drop.clone(), drop.getAmount());
            }
        }

        for (Map.Entry<ItemStack, Integer> entry : totalDrops.entrySet()) {
            ItemStack item = entry.getKey();
            int totalAmount = entry.getValue();

            condenseManager.addItem(player, item, totalAmount);
            int current = condenseManager.getItemAmount(player, item);

            player.sendActionBar(prefix + "+" + totalAmount + ChatColor.GRAY
                    + " (" + ChatColor.GREEN + current + ChatColor.GRAY + " / " + ChatColor.GREEN + "2,000" + ChatColor.GRAY + " )");
        }
    }
}
