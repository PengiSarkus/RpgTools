# RpgTools

A custom Minecraft Spigot/Paper plugin designed to completely overhaul the tool and weapon experience system. 

## Features

### ⚔️ Custom Weapon & Tool Leveling
* **Mining Tools:** Earn custom XP by mining specific ores (e.g., Coal, Diamonds, Ancient Debris).
* **Melee Weapons:** Earn custom XP by killing mobs and players with swords or axes.
* **Dynamic Lore:** Tools automatically track their current Level and XP in the item's lore.

### 🛡️ Anti-Exploit System (Silk Touch Proof)
* **Persistent Data Tracking:** Uses Spigot's PersistentDataContainer (PDC) to secretly tag player-placed ores in chunks using highly optimized binary bitwise packing.
* **Piston-Proof:** Cross-chunk piston movement tracking prevents players from using pistons to "cleanse" player-placed ores.
* **Exploit Denied:** Players cannot farm XP by repeatedly placing and breaking the same ores with Silk Touch.

### 🏪 Interactive GUI Market
* Features a fully interactive `/market` menu built from the ground up.
* Dedicated menus for Tool Leveling and Melee Weapon Leveling.
* Custom Inventory Holders and metadata tracking to safely route clicks and prevent item theft.

## Commands
* `/market` - Opens the main RPG Tools market interface.
