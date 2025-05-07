package com.mills.toggleCondense;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {

        CondenseManager condenseManager = new CondenseManager();
        Bukkit.getPluginManager().registerEvents(new CondenseListener(condenseManager), this);
        getCommand("togglecondense").setExecutor(new CondenseCommand(condenseManager));
    }

    @Override
    public void onDisable() {
    }
}
