package com.sarkus.rpgTools;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

/**
 * Manages the experience (XP) and leveling system for tools and weapons.
 * It reads and writes XP and level data directly to the item's PersistentDataContainer.
 */
public class RpgToolManager {

    private final RpgTools plugin;
    private final NamespacedKey xpKey;
    private final NamespacedKey levelKey;

    /**
     * Constructs a new RpgToolManager.
     *
     * @param plugin The main plugin instance used for NamespacedKeys.
     */
    public RpgToolManager(RpgTools plugin) {
        this.plugin = plugin;
        this.xpKey = new NamespacedKey(plugin, "xp");
        this.levelKey = new NamespacedKey(plugin, "level");
    }

    /**
     * Gets the current XP of the specified item.
     *
     * @param item The item to check.
     * @return The current XP, or 0 if none is set.
     */
    public int getXp(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().getOrDefault(xpKey, PersistentDataType.INTEGER, 0);
    }

    /**
     * Gets the current level of the specified item.
     *
     * @param item The item to check.
     * @return The current level, or 1 if none is set.
     */
    public int getLevel(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().getOrDefault(levelKey, PersistentDataType.INTEGER, 1);
    }

    /**
     * Adds XP to an item and handles leveling up if the required XP threshold is met.
     * Updates the item's lore accordingly.
     *
     * @param userItem The item receiving XP.
     * @param xpAmount The amount of XP to add.
     * @param player   The player holding the item (used for notifications).
     */
    public void addXp(ItemStack userItem, int xpAmount, Player player) {
        // Retrieve the ItemMeta of the item. If it's null (e.g. air or invalid item), abort.
        ItemMeta meta = userItem.getItemMeta();
        if (meta == null) return;

        // The container holds custom data for the item (like our xp and level)
        PersistentDataContainer container = meta.getPersistentDataContainer();

        // Get the current XP and level. If they don't exist yet, default to 0 and 1.
        int xp = getXp(userItem);
        int level = getLevel(userItem);

        // Add the newly gained XP
        xp += xpAmount;

        // Check if the new XP exceeds the requirement for the next level
        // The formula for required XP is: (current level) * 75
        if (xp >= level * 75) {
            xp = 0; // Reset XP upon leveling up
            level++; // Increment the level
            
            // Notify the player
            player.sendMessage("§aYour tool leveled up to " + level + "!");
        }

        // Save the updated XP and level back into the PersistentDataContainer
        container.set(xpKey, PersistentDataType.INTEGER, xp);
        container.set(levelKey, PersistentDataType.INTEGER, level);

        // Apply the updated meta back to the item
        userItem.setItemMeta(meta);
        
        // Refresh the lore so the player can see the new XP and Level visually
        updateLore(userItem);
    }

    /**
     * Forcibly sets the level of an item and updates its lore.
     *
     * @param userItem The item to modify.
     * @param level    The new level.
     * @param player   The player holding the item.
     */
    public void setLevel(ItemStack userItem, int level, Player player) {
        ItemMeta meta = userItem.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        
        // Overwrite the level key with the provided level
        container.set(levelKey, PersistentDataType.INTEGER, level);
        userItem.setItemMeta(meta);
        
        // Refresh the lore to show the new level
        updateLore(userItem);
    }

    /**
     * Updates the lore of an item to display its current level and XP.
     *
     * @param item The item whose lore should be updated.
     */
    public void updateLore(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        
        // Retrieve the most recent XP and level values from the item
        int xp = getXp(item);
        int level = getLevel(item);

        // Set the lore (description lines below the item name) to show the stats
        // Uses color codes (§7 for gray) and calculates max XP visually as (level * 100)
        meta.setLore(Arrays.asList(
                "§7Level: " + level,
                "§7XP: " + xp + " / " + (level * 75) // Note: Changed to match the level * 75 logic used in addXp
        ));
        
        // Save the updated lore to the item
        item.setItemMeta(meta);
    }
}