package com.mills.toggleCondense;

import com.mills.toggleCondense.Commands.CondenseAdmin;
import com.mills.toggleCondense.Commands.CondenseAdminTabCompleter;
import com.mills.toggleCondense.Commands.CondenseCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main instance;
    private AllowedItems allowedItems;
    private CondenseManager condenseManager;

    @Override
    public void onEnable() {

        instance = this;

        allowedItems = new AllowedItems(this.getDataFolder());
        condenseManager = new CondenseManager();

        Bukkit.getPluginManager().registerEvents(new CondenseListener(this), this);
        Bukkit.getPluginManager().registerEvents(new XPManager(), this);
        getCommand("togglecondense").setExecutor(new CondenseCommand(condenseManager));
        getCommand("tcadmin").setExecutor(new CondenseAdmin(this));
        getCommand("tcadmin").setTabCompleter(new CondenseAdminTabCompleter());
    }

    @Override
    public void onDisable() {
    }

    public static Main getInstance() {
        return instance;
    }

    public AllowedItems getAllowedItems() {
        return allowedItems;
    }

    public CondenseManager getCondenseManager() {
        return condenseManager;
    }
}
