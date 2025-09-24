package com.sarkus.rpgTools;

import com.sarkus.rpgTools.Gui.GuiCommand;
import com.sarkus.rpgTools.Gui.GuiListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class RpgTools extends JavaPlugin {
    private RpgToolManager manager;
    @Override
    public void onEnable() {
        manager = new RpgToolManager(this);
        getLogger().info("RpgTools is enabled!");
        GuiCommand guiCommand = new GuiCommand(this);
        getPlugin().getCommand("market").setExecutor(guiCommand);
        getServer().getPluginManager().registerEvents(new XpHandler(manager), this);
        getServer().getPluginManager().registerEvents(new GuiListener(guiCommand), this);
    }

    @Override
    public void onDisable() {
    }
    public static JavaPlugin getPlugin() {
        return JavaPlugin.getPlugin(RpgTools.class);
    }
}
