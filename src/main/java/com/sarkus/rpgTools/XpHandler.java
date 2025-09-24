package com.sarkus.rpgTools;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class XpHandler implements Listener {
    private RpgToolManager manager;

    public XpHandler(RpgToolManager manager) {
        this.manager = manager;
    }
    public boolean endsWithChecker(String checker,Player player) {
        ItemStack userItem = player.getInventory().getItemInMainHand();
        if (userItem.getType().toString().endsWith(checker)) {
            return true;
        }else{
            return false;
        }

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack userItem = player.getInventory().getItemInMainHand();
        if (endsWithChecker("_PICKAXE",player) || endsWithChecker("_SHOVEL",player) || endsWithChecker("_AXE",player) || endsWithChecker("_HOE",player)) {
            manager.addXp(userItem, 10, player);
            player.sendActionBar("§a+" + 10 + " XP");
        }
    }
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() instanceof Player) {
            Player player = (Player) event.getEntity().getKiller();
            ItemStack userItem = player.getInventory().getItemInMainHand();
            if (endsWithChecker("_SWORD", player)|| endsWithChecker("_AXE", player)) {
                manager.addXp(userItem, 10, player);
                player.sendActionBar("§a+" + 10 + " XP");
            }

        }
    }
}
