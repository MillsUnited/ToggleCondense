package com.mills.toggleCondense;

import org.bukkit.Bukkit;
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
    private AllowedItems allowedItems;

    public CondenseListener(Main main) {
        this.condenseManager = main.getCondenseManager();
        this.allowedItems = main.getAllowedItems();

        Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
            long now = System.currentTimeMillis();
            Iterator<Map.Entry<UUID, Long>> iterator = cooldown.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<UUID, Long> entry = iterator.next();
                long time = entry.getValue();
                if (now - time >= 30_000) {
                    UUID uuid = entry.getKey();
                    Player player = Bukkit.getPlayer(uuid);
                    if (player != null && player.isOnline()) {
                        condenseManager.refundUncondensedItems(player);
                        player.sendMessage(condenseManager.prefix + "Your condensed items have been returned to your inventory due to inactivity!");
                    }
                    iterator.remove();
                }
            }
        }, 20L * 5, 20L * 5);
    }

    private String prefix = ChatColor.translateAlternateColorCodes('&', "&6&lCondense &8» &a");
    private static Map<UUID, Long> cooldown = new HashMap<>();

    public static void clearPlayerCooldown(Player player) {
        cooldown.remove(player.getUniqueId());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (!condenseManager.isCondenseEnabled(player)) return;
        if (!player.getGameMode().equals(GameMode.SURVIVAL)) return;

        Block block = e.getBlock();
        Collection<ItemStack> drops = block.getDrops(player.getInventory().getItemInMainHand());

        Map<ItemStack, Integer> totalDrops = new HashMap<>();

        for (ItemStack drop : drops) {
            if (drop == null || drop.getType().isAir()) continue;
            if (!allowedItems.isItem(drop)) continue;
            e.setDropItems(false);

            boolean matched = false;
            for (ItemStack key : totalDrops.keySet()) {
                if (key.isSimilar(drop)) {
                    cooldown.put(player.getUniqueId(), System.currentTimeMillis());
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
        if (!condenseManager.isCondenseEnabled(player)) return;

        List<ItemStack> drops = new ArrayList<>(e.getDrops());

        Map<ItemStack, Integer> totalDrops = new HashMap<>();

        for (ItemStack drop : drops) {
            if (drop == null || drop.getType().isAir()) continue;
            if (!allowedItems.isItem(drop)) continue;
            e.getDrops().clear();

            boolean matched = false;
            for (ItemStack key : totalDrops.keySet()) {
                if (key.isSimilar(drop)) {
                    cooldown.put(player.getUniqueId(), System.currentTimeMillis());
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
            int islandDropMulti = IslandUtil.getMobDropMultiplier(player);
            int totalAmount = ( entry.getValue() * islandDropMulti );

            condenseManager.addItem(player, item, totalAmount);
            int current = condenseManager.getItemAmount(player, item);

            player.sendActionBar(prefix + "+" + totalAmount + ChatColor.GRAY
                    + " (" + ChatColor.GREEN + current + ChatColor.GRAY + " / " + ChatColor.GREEN + "2,000" + ChatColor.GRAY + " )");
        }
    }
}
