package com.mills.toggleCondense;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import org.bukkit.entity.Player;

public class IslandUtil {

    public static int getMobDropMultiplier(Player player) {
        Island island = SuperiorSkyblockAPI.getPlayer(player).getIsland();

        if (island == null) return 1;

        return (int) island.getMobDropsMultiplier();
    }
}
