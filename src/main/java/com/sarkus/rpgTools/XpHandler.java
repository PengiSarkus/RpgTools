package com.sarkus.rpgTools;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class XpHandler implements Listener {
    private RpgToolManager manager;

    public XpHandler(RpgToolManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack userItem = player.getInventory().getItemInMainHand();

        if (userItem.getType().toString().endsWith("_PICKAXE")) {
            manager.addXp(userItem, 30, player);
            player.sendActionBar("§a+" + 10 + " XP");
        }
    }
}
