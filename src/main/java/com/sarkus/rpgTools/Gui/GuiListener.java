package com.sarkus.rpgTools.Gui;

import com.sarkus.rpgTools.RpgTools;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class GuiListener implements Listener {
    private final GuiCommand gui;
    public GuiListener(GuiCommand gui) {
        this.gui = gui;
    }
   @EventHandler
   public void onInventoryClick(InventoryClickEvent e) {
       if (!(e.getWhoClicked() instanceof Player)) {return;}
       Player p = (Player) e.getWhoClicked();
       if(p.hasMetadata("openedMenu")) {
           e.setCancelled(true);
           String menu = p.getMetadata("openedMenu").get(0).asString();
           int slot = e.getSlot();
           switch (menu) {
               case "mainInventory":
                   handleMainInventory(p, slot);
                   e.setCancelled(true);
                   break;

               case "levelMarketInventory":
                   handleLevelMarket(p, slot);
                   e.setCancelled(true);
                   break;

               case "levelMarketTools":
              //     handleLevelMarketTools(p, slot);
                   e.setCancelled(true);
                   break;

               case "miningToolInventory":
               //    handleMiningTool(p, slot);
                   e.setCancelled(true);
                   break;

               case "miningToolVanillaInventory":
             //      handleMiningToolVanilla(p, slot);
                   e.setCancelled(true);
                   break;

               case "miningToolCustomInventory":
              //     handleMiningToolCustom(p, slot);
                   e.setCancelled(true);
                   break;

               case "meleeInventory":
               //    handleMelee(p, slot);
                   e.setCancelled(true);
                   break;

               case "meleeVanillaInventory":
                //   handleMeleeVanilla(p, slot);
                   e.setCancelled(true);
                   break;

               case "meleeCustomInventory":
                 //  handleMeleeCustom(p, slot);
                   e.setCancelled(true);
                   break;

               case "axeInventory":
              //     handleAxe(p, slot);
                   e.setCancelled(true);
                   break;

               case "axeVanillaInventory":
               //    handleAxeVanilla(p, slot);
                   e.setCancelled(true);
                   break;

               case "axeCustomInventory":
               //    handleAxeCustom(p, slot);
                   e.setCancelled(true);
                   break;
           }
       }


   }
   @EventHandler
   public void onInventoryClose(InventoryCloseEvent e) {
       Player player = (Player) e.getPlayer();
       if(player.hasMetadata("openedMenu")) player.removeMetadata("openedMenu", RpgTools.getPlugin());
   }
    public void handleMainInventory(Player player, int slot){
        switch(slot) {
            case 11:
                player.removeMetadata("openedMenu", RpgTools.getPlugin());
                player.setMetadata("openedMenu",new FixedMetadataValue(RpgTools.getPlugin(),"levelMarketInventory"));
                player.openInventory(gui.getLevelMarketInventory());
        }
    }
    public void handleLevelMarket(Player player, int slot){
        switch(slot) {
            case 11:
                player.removeMetadata("openedMenu", RpgTools.getPlugin());
                player.setMetadata("openedMenu", new FixedMetadataValue(RpgTools.getPlugin(),"miningToolInventory"));
                player.openInventory(gui.getMiningToolInventory());
        }
    }
}
