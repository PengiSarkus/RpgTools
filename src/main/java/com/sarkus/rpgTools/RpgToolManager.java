package com.sarkus.rpgTools;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class RpgToolManager {

    private final RpgTools plugin;
    private final NamespacedKey xpKey;
    private final NamespacedKey levelKey;

    public RpgToolManager(RpgTools plugin) {
        this.plugin = plugin;
        this.xpKey = new NamespacedKey(plugin, "xp");
        this.levelKey = new NamespacedKey(plugin, "level");
    }

    public int getXp(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().getOrDefault(xpKey, PersistentDataType.INTEGER, 0);
    }

    public int getLevel(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().getOrDefault(levelKey, PersistentDataType.INTEGER, 1);
    }

    public void addXp(ItemStack userItem, int xpAmount, Player player) {
        ItemMeta meta = userItem.getItemMeta();
        if (meta == null) return;

        PersistentDataContainer container = meta.getPersistentDataContainer();

        int xp = container.getOrDefault(xpKey, PersistentDataType.INTEGER, 0);
        int level = container.getOrDefault(levelKey, PersistentDataType.INTEGER, 1);

        xp += xpAmount;

        if (xp >= level * 75) {
            xp = 0;
            level++;
            player.sendMessage("§aYour tool leveled up to " + level + "!");
        }

        container.set(xpKey, PersistentDataType.INTEGER, xp);
        container.set(levelKey, PersistentDataType.INTEGER, level);

        userItem.setItemMeta(meta);
        updateLore(userItem);
    }
    public void setLevel(ItemStack userItem, int level, Player player) {
        ItemMeta meta = userItem.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(levelKey, PersistentDataType.INTEGER, level);
        userItem.setItemMeta(meta);
        updateLore(userItem);
    }

    public void updateLore(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        int xp = getXp(item);
        int level = getLevel(item);

        meta.setLore(Arrays.asList(
                "§7Level: " + level,
                "§7XP: " + xp + " / " + (level * 100)
        ));
        item.setItemMeta(meta);
    }
}