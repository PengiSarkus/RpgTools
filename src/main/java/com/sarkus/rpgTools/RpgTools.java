package com.sarkus.rpgTools;

import org.bukkit.plugin.java.JavaPlugin;

public final class RpgTools extends JavaPlugin {
    private RpgToolManager manager;
    @Override
    public void onEnable() {
        manager = new RpgToolManager(this);
        getLogger().info("RpgTools is enabled!");
        getServer().getPluginManager().registerEvents(new XpHandler(manager), this);
    }

    @Override
    public void onDisable() {
    }
}
