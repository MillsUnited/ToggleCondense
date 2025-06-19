package com.mills.toggleCondense;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class XPManager implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        LivingEntity entity = e.getEntity();
        Player player = entity.getKiller();

        if (player == null) return;

        int droppedXP = e.getDroppedExp();
        e.setDroppedExp(0);
        player.giveExp(droppedXP, true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();

        int droppedXP = e.getExpToDrop();
        e.setExpToDrop(0);
        player.giveExp(droppedXP);
    }
}
