package com.sarkus.rpgTools;

import com.sarkus.rpgTools.Gui.GuiCommand;
import com.sarkus.rpgTools.Gui.GuiListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class for the RpgTools plugin.
 * Handles the initialization of commands, listeners, and managers.
 */
public final class RpgTools extends JavaPlugin {
    private RpgToolManager manager;

    /**
     * Called when the plugin is enabled.
     * Initializes the manager, commands, and registers event listeners.
     */
    @Override
    public void onEnable() {
        // Initialize the manager that handles XP and leveling logic
        manager = new RpgToolManager(this);
        
        // Log to the console that the plugin has started successfully
        getLogger().info("RpgTools is enabled!");
        
        // Initialize the command executor for the GUI market
        GuiCommand guiCommand = new GuiCommand(this);
        
        // Register the /market command and attach the GuiCommand executor to it
        getPlugin().getCommand("market").setExecutor(guiCommand);
        
        // Register the event listener for handling block breaks and entity kills to give XP
        getServer().getPluginManager().registerEvents(new XpHandler(manager), this);
        
        // Register the event listener for handling clicks inside the /market GUI
        getServer().getPluginManager().registerEvents(new GuiListener(guiCommand), this);
    }

    /**
     * Called when the plugin is disabled.
     */
    @Override
    public void onDisable() {
    }

    /**
     * Gets the main plugin instance.
     *
     * @return the RpgTools plugin instance
     */
    public static JavaPlugin getPlugin() {
        return JavaPlugin.getPlugin(RpgTools.class);
    }
}
