package com.sarkus.rpgTools;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

/**
 * Listens for game events to award XP to the player's held item.
 * Supports mining ores for tools and killing entities for weapons.
 */
public class XpHandler implements Listener {
    private RpgToolManager manager;
    private final HashMap<Material, Integer> blockXpList = new HashMap<>();
    private final NamespacedKey placedOresKey = new NamespacedKey(RpgTools.getPlugin(), "placed_ores");

    /**
     * Constructs the XpHandler with preset XP values for different ores.
     *
     * @param manager The RpgToolManager instance to handle XP additions.
     */
    public XpHandler(RpgToolManager manager) {
        this.manager = manager;
        blockXpList.put(Material.COAL_ORE, 5);
        blockXpList.put(Material.DEEPSLATE_COAL_ORE, 5);
        blockXpList.put(Material.NETHER_GOLD_ORE, 15);
        blockXpList.put(Material.REDSTONE_ORE, 8);
        blockXpList.put(Material.DEEPSLATE_REDSTONE_ORE, 8);
        blockXpList.put(Material.LAPIS_ORE, 10);
        blockXpList.put(Material.DEEPSLATE_LAPIS_ORE, 10);
        blockXpList.put(Material.NETHER_QUARTZ_ORE, 3);
        blockXpList.put(Material.DIAMOND_ORE, 30);
        blockXpList.put(Material.DEEPSLATE_DIAMOND_ORE, 25);
        blockXpList.put(Material.EMERALD_ORE, 50);
        blockXpList.put(Material.DEEPSLATE_EMERALD_ORE, 50);
        blockXpList.put(Material.ANCIENT_DEBRIS, 100);
        blockXpList.put(Material.IRON_ORE, 8);
        blockXpList.put(Material.DEEPSLATE_IRON_ORE, 8);
        blockXpList.put(Material.COPPER_ORE, 3);
        blockXpList.put(Material.DEEPSLATE_COPPER_ORE, 3);
        blockXpList.put(Material.GOLD_ORE, 20);
        blockXpList.put(Material.DEEPSLATE_GOLD_ORE, 20);
        blockXpList.put(Material.ACACIA_LOG, 5);
    }

    /**
     * Checks if the player's held item type ends with a specific string (e.g.,
     * "_PICKAXE").
     *
     * @param checker The string suffix to check for.
     * @param player  The player holding the item.
     * @return True if the item's type ends with the checker, false otherwise.
     */
    public boolean endsWithChecker(String checker, Player player) {
        // Get the item currently held in the player's main hand
        ItemStack userItem = player.getInventory().getItemInMainHand();

        // Convert the material type enum to string (e.g. "DIAMOND_PICKAXE") and check
        // suffix
        if (userItem.getType().toString().endsWith(checker)) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Handles block breaking events to award XP to mining tools.
     * Only awards XP for predefined ores in blockXpList.
     *
     * @param event The BlockBreakEvent.
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.isDropItems() == true) return;
        // Player placed ore checker
        Block eventBlock = event.getBlock();
        Chunk eventChunk = event.getBlock().getChunk();
        PersistentDataContainer chunkPdc = eventChunk.getPersistentDataContainer();
        int packedBlockCoordinates = packBlock(eventBlock);
        int[] existing = chunkPdc.get(placedOresKey, PersistentDataType.INTEGER_ARRAY);
        boolean wasPlacedByPlayer = false;
        if (!(existing == null)) {
            for (int i = 0; i < existing.length; i++) {
                if (existing[i] == packedBlockCoordinates) {
                    wasPlacedByPlayer = true;
                    break;
                }
            }
        }
        if (wasPlacedByPlayer) {
            if (existing.length == 1) {
                chunkPdc.remove(placedOresKey);
            } else {
                int cursor = 0;
                int[] newArray = new int[existing.length - 1];
                for (int i = 0; i < existing.length; i++) {
                    if (!(existing[i] == packedBlockCoordinates)) {
                        newArray[cursor] = existing[i];
                        cursor++;
                    }
                }
                chunkPdc.set(placedOresKey, PersistentDataType.INTEGER_ARRAY, newArray);
            }
            event.getPlayer().sendMessage("§cYou cannot get XP from player-placed ores!");
            return;
        }


        Player player = event.getPlayer();
        ItemStack userItem = player.getInventory().getItemInMainHand();
        // Ensure the player is holding a valid tool (pickaxe, shovel, axe, or hoe)
        if (endsWithChecker("_PICKAXE", player) || endsWithChecker("_SHOVEL", player) || endsWithChecker("_AXE", player)
                || endsWithChecker("_HOE", player)) {
            // Check if the block broken is one of the ores mapped in our blockXpList
            if (blockXpList.containsKey(eventBlock.getType())) {
                // Retrieve the configured XP reward for this specific block type
                int xpReward = Integer.valueOf(blockXpList.get(eventBlock.getType()));
                // Add the XP to the player's held tool
                manager.addXp(userItem, xpReward, player);

                // Send a quick action bar message so the player knows they earned XP
                player.sendActionBar("§a+" + xpReward + " XP");
            }

        }
    }

    /**
     * Handles block placing events.
     * Records the exact chunk-relative coordinates of placed ores into the chunk's
     * PersistentDataContainer. This prevents players from farming XP by repeatedly
     * placing and breaking the same ores with Silk Touch.
     *
     * @param event The BlockPlaceEvent.
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!blockXpList.containsKey(event.getBlock().getType())) return;
        Chunk chunk = event.getBlockPlaced().getChunk();
        PersistentDataContainer chunkPdc = chunk.getPersistentDataContainer();
        int packedCoordinates = packBlock(event.getBlock());
        int[] existingOres = chunkPdc.get(placedOresKey, PersistentDataType.INTEGER_ARRAY);
        int[] newOres;
        if (existingOres == null) {
            newOres = new int[]{packedCoordinates};
        } else {
            newOres = new int[existingOres.length + 1];
            System.arraycopy(existingOres, 0, newOres, 0, existingOres.length);
            newOres[existingOres.length] = packedCoordinates;
        }
        chunkPdc.set(placedOresKey, PersistentDataType.INTEGER_ARRAY, newOres);
    }

    /**
     * Handles piston extend events to prevent players from exploiting the XP system.
     * If a player pushes a placed ore with a piston, this updates the chunk's
     * PersistentDataContainer with the new coordinates. This also safely handles
     * ores being pushed across chunk boundaries.
     *
     * @param event The BlockPistonExtendEvent.
     */
    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        for (Block oldBlock : event.getBlocks()) {
            if (!blockXpList.containsKey(oldBlock.getType())) continue;
            Chunk oldChunk = oldBlock.getChunk();
            Block newBlock = oldBlock.getRelative(event.getDirection());
            Chunk newChunk = newBlock.getChunk();
            int[] oldChunkPlacedOres = oldChunk.getPersistentDataContainer().get(placedOresKey, PersistentDataType.INTEGER_ARRAY);
            if (oldChunkPlacedOres == null) continue;
            for (int oreCoord : oldChunkPlacedOres) {
                if (oreCoord == packBlock(oldBlock)) {
                    if (oldChunkPlacedOres.length == 1) oldChunk.getPersistentDataContainer().remove(placedOresKey);
                    else {
                        int cursor = 0;
                        int[] oldChunkUpdatedPlacedOres = new int[oldChunkPlacedOres.length - 1];
                        for (int i = 0; i < oldChunkPlacedOres.length; i++) {
                            if (!(oldChunkPlacedOres[i] == oreCoord)) {
                                oldChunkUpdatedPlacedOres[cursor] = oldChunkPlacedOres[i];
                                cursor++;
                            }
                        }
                        oldChunk.getPersistentDataContainer().set(placedOresKey, PersistentDataType.INTEGER_ARRAY, oldChunkUpdatedPlacedOres);
                    }
                    if (!newChunk.getPersistentDataContainer().has(placedOresKey, PersistentDataType.INTEGER_ARRAY)) {
                        int[] newChunkUpdatedPlacedOres = new int[1];
                        newChunkUpdatedPlacedOres[0] = packBlock(newBlock);
                        newChunk.getPersistentDataContainer().set(placedOresKey, PersistentDataType.INTEGER_ARRAY, newChunkUpdatedPlacedOres);
                    } else {
                        int[] newChunkPlacedOres = newChunk.getPersistentDataContainer().get(placedOresKey, PersistentDataType.INTEGER_ARRAY);
                        int[] newChunkUpdatedPlacedOres = new int[newChunkPlacedOres.length + 1];
                        System.arraycopy(newChunkPlacedOres, 0, newChunkUpdatedPlacedOres, 0, newChunkPlacedOres.length);
                        newChunkUpdatedPlacedOres[newChunkPlacedOres.length] = packBlock(newBlock);
                        newChunk.getPersistentDataContainer().set(placedOresKey, PersistentDataType.INTEGER_ARRAY, newChunkUpdatedPlacedOres);
                    }
                }
            }
        }
    }

    /**
     * Handles piston retract events (Sticky Pistons pulling blocks).
     * Works exactly like the extend event to prevent players from exploiting the XP system.
     * If a Sticky Piston pulls a player-placed ore, this updates the chunk's
     * PersistentDataContainer with the new coordinates, handling cross-chunk movement safely.
     *
     * @param event The BlockPistonRetractEvent.
     */
    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {
        for (Block oldBlock : event.getBlocks()) {
            if (!blockXpList.containsKey(oldBlock.getType())) continue;
            Chunk oldChunk = oldBlock.getChunk();
            Block newBlock = oldBlock.getRelative(event.getDirection());
            Chunk newChunk = newBlock.getChunk();
            PersistentDataContainer oldChunkPdc = oldChunk.getPersistentDataContainer();
            PersistentDataContainer newChunkPdc = newChunk.getPersistentDataContainer();
            int[] oldChunkPlacedOres = oldChunkPdc.get(placedOresKey, PersistentDataType.INTEGER_ARRAY);
            if (!oldChunkPdc.has(placedOresKey, PersistentDataType.INTEGER_ARRAY)) continue;
            for (int oreCoord : oldChunkPlacedOres) {
                if (oreCoord == packBlock(oldBlock)) {
                    if (oldChunkPlacedOres.length == 1) {
                        oldChunkPdc.remove(placedOresKey);
                    } else {
                        int cursor = 0;
                        int[] oldChunkUpdatedPlacedOres = new int[oldChunkPlacedOres.length - 1];
                        for (int i = 0; i < oldChunkPlacedOres.length; i++) {
                            if (!(oldChunkPlacedOres[i] == packBlock(oldBlock))) {
                                oldChunkUpdatedPlacedOres[cursor] = oldChunkPlacedOres[i];
                                cursor++;
                            }
                        }
                        oldChunkPdc.set(placedOresKey, PersistentDataType.INTEGER_ARRAY, oldChunkUpdatedPlacedOres);
                    }
                    int[] newChunkPlacedOres = newChunkPdc.get(placedOresKey, PersistentDataType.INTEGER_ARRAY);
                    if (!newChunkPdc.has(placedOresKey, PersistentDataType.INTEGER_ARRAY)) {
                        int[] newChunkUpdatedPlacedOres = new int[1];
                        newChunkUpdatedPlacedOres[0] = packBlock(newBlock);
                        newChunk.getPersistentDataContainer().set(placedOresKey, PersistentDataType.INTEGER_ARRAY, newChunkUpdatedPlacedOres);
                    } else {
                        int[] newChunkUpdatedPlacedOres = new int[newChunkPlacedOres.length + 1];
                        System.arraycopy(newChunkPlacedOres, 0, newChunkUpdatedPlacedOres, 0, newChunkPlacedOres.length);
                        newChunkUpdatedPlacedOres[newChunkPlacedOres.length] = packBlock(newBlock);
                        newChunk.getPersistentDataContainer().set(placedOresKey, PersistentDataType.INTEGER_ARRAY, newChunkUpdatedPlacedOres);
                    }

                }
            }
        }
    }

    /**
     * Handles entity death events to award XP to melee weapons.
     * Awards 10 XP per kill when using a sword or axe.
     *
     * @param event The EntityDeathEvent.
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        // Check if the entity was killed by a player (not environmental damage or other
        // mobs)
        if (event.getEntity().getKiller() instanceof Player) {
            Player player = (Player) event.getEntity().getKiller();
            ItemStack userItem = player.getInventory().getItemInMainHand();

            // Ensure the killer is holding a weapon (sword or axe)
            if (endsWithChecker("_SWORD", player) || endsWithChecker("_AXE", player)) {

                // Grant a fixed amount of 10 XP per kill
                manager.addXp(userItem, 10, player);

                // Show the XP gained on the action bar
                player.sendActionBar("§a+" + 10 + " XP");
            }

        }
    }
    //Helper Methods


    //Helps with optimization, packs the coordinate details into one number, shifts them accordingly
    //essentially a helper method for BlockPlaceEvent, detecting player placed blocks
    public int packBlock(Block block) {
        int relX = block.getX() & 15;
        int relZ = block.getZ() & 15;
        int relY = block.getY() + 64;
        return relY | relX << 9 | relZ << 13;
    }
}
