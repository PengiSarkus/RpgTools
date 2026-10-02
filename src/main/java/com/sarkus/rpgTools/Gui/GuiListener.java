package com.sarkus.rpgTools.Gui;

import com.sarkus.rpgTools.RpgTools;
import org.bukkit.Bukkit;
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
       // Only process clicks made by a player
       if (!(e.getWhoClicked() instanceof Player)) {return;}
       Player p = (Player) e.getWhoClicked();
       
       // Check if the player is currently viewing our custom GUI (using metadata tags)
       if(p.hasMetadata("openedMenu") || p.hasMetadata("marketValue")) {
           // Cancel the event so they can't take items out of the custom GUI
           e.setCancelled(true);
           
           // Determine which specific menu page they are currently on
           String menu = p.getMetadata("marketValue").get(0).asString();
           int slot = e.getSlot();
           
           // Route the click to the correct handler based on the active menu
           switch (menu) {
               case "mainInventory":
                   handleMainInventory(p, slot);
                   e.setCancelled(true);
                   break;

               case "levelMarketInventory":
                   handleLevelMarket(p, slot);
                   e.setCancelled(true);
                   break;

               // Note: Other GUI menus below currently don't have functional handlers.
               // They just cancel the click to protect the GUI items.
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
       // When the player closes the GUI, make sure to clean up the metadata
       // so they can use their inventory normally again.
       if(player.hasMetadata("openedMenu")) player.removeMetadata("openedMenu", RpgTools.getPlugin());
       if(player.hasMetadata("marketValue")) player.removeMetadata("marketValue", RpgTools.getPlugin());
   }
    public void handleMainInventory(Player player, int slot){
        switch(slot) {
            case 11:
                // They clicked the "Level Market" button. Open the next menu.
                player.openInventory(gui.getLevelMarketInventory());
                // Update their current menu state to the new menu
                player.setMetadata("marketValue", new FixedMetadataValue(RpgTools.getPlugin(),"levelMarketInventory"));
                break;
            case 15:
                // They clicked the "Close Barrier" button. Clean up state and close the UI.
                player.removeMetadata("marketValue", RpgTools.getPlugin());
                player.removeMetadata("openedMenu", RpgTools.getPlugin());
                player.closeInventory();
        }
    }
    public void handleLevelMarket(Player player, int slot){
        switch(slot) {
            case 11:
                    // They clicked the "Tool Level Market" option.
                    player.openInventory(gui.getMiningToolInventory());
                    player.setMetadata("marketValue", new FixedMetadataValue(RpgTools.getPlugin(), "miningToolInventory"));
                    player.setMetadata("openedMenu" , new FixedMetadataValue(RpgTools.getPlugin(),"levelMarketInventory"));
                    break;
            case 15:
                // They clicked the "Melee Weapon Level Market" option.
                player.openInventory(gui.getMeleeInventory());
                player.setMetadata("marketValue", new FixedMetadataValue(RpgTools.getPlugin(), "meleeInventory"));
                player.setMetadata("openedMenu" , new FixedMetadataValue(RpgTools.getPlugin(),"meleeInventory"));
        }
    }
}
