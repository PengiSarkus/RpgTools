package com.sarkus.rpgTools.Gui;

import com.sarkus.rpgTools.RpgTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class GuiCommand implements CommandExecutor {
    private final Inventory mainInventory;
    private final Inventory miningToolInventory;
    private final Inventory miningToolVanillaInventory;
    private final Inventory miningToolCustomInventory;
    private final Inventory axeInventory;
    private final Inventory axeVanillaInventory;
    private final Inventory axeCustomInventory;
    private final Inventory meleeInventory;
    private final Inventory meleeVanillaInventory;
    private final Inventory meleeCustomInventory;
    private final Inventory levelMarketInventory;


    private HashMap<String,Integer> toolEnchantments = new HashMap<String,Integer>();
    private HashMap<String,Integer> meleeEnchantments = new HashMap<String,Integer>();
    private HashMap<String,Integer> axeEnchantments = new HashMap<String,Integer>();


    public GuiCommand(JavaPlugin plugin){
        this.axeInventory = Bukkit.createInventory(null, 9*3, ChatColor.DARK_RED + "Market");
        this.meleeInventory = Bukkit.createInventory(null, 9*3, ChatColor.DARK_RED + "Market");
        this.mainInventory = Bukkit.createInventory(null,9*3, ChatColor.DARK_RED+ "Market");
        this.miningToolInventory = Bukkit.createInventory(null,9*3, ChatColor.DARK_RED + "Market");
        this.miningToolVanillaInventory = Bukkit.createInventory(null,9*3,ChatColor.DARK_RED + "Market" );
        this.miningToolCustomInventory = Bukkit.createInventory(null,9*3,ChatColor.DARK_RED + "Market" );
        this.axeCustomInventory = Bukkit.createInventory(null,9*3,ChatColor.DARK_RED + "Market" );
        this.axeVanillaInventory = Bukkit.createInventory(null,9*3,ChatColor.DARK_RED + "Market" );
        this.meleeVanillaInventory = Bukkit.createInventory(null,9*3,ChatColor.DARK_RED + "Market" );
        this.meleeCustomInventory = Bukkit.createInventory(null,9*3,ChatColor.DARK_RED + "Market" );
        this.levelMarketInventory = Bukkit.createInventory(null,9*3,ChatColor.DARK_RED + "Market" );


        //toolenchanments hashmap
        toolEnchantments.put("Mending",1);
        toolEnchantments.put("Unbreaking",3);
        toolEnchantments.put("Efficiency", 5);
        toolEnchantments.put("Fortune",3);
        toolEnchantments.put("Silk Touch",1);

        //meleeEnchantments hashmap
        meleeEnchantments.put("Bane of Arthropods", 5);
        meleeEnchantments.put("Breach", 4);
        meleeEnchantments.put("Density", 5);
        meleeEnchantments.put("Efficiency", 5);
        meleeEnchantments.put("Fire Aspect", 2);
        meleeEnchantments.put("Looting", 3);
        meleeEnchantments.put("Impaling", 5);
        meleeEnchantments.put("Knockback", 2);
        meleeEnchantments.put("Sharpness", 5);
        meleeEnchantments.put("Smite", 5);
        meleeEnchantments.put("Sweeping Edge", 3);
        meleeEnchantments.put("Wind Burst", 3);
        meleeEnchantments.put("Mending",1);
        meleeEnchantments.put("Unbreaking",3);



        //axe enchantments
        axeEnchantments.put("Bane of Arthropods", 5);
        axeEnchantments.put("Efficiency", 5);
        axeEnchantments.put("Fire Aspect", 2);
        axeEnchantments.put("Fortune", 3);
        axeEnchantments.put("Knockback", 2);
        axeEnchantments.put("Mending", 1);
        axeEnchantments.put("Sharpness", 5);
        axeEnchantments.put("Silk Touch", 1);
        axeEnchantments.put("Smite", 5);
        axeEnchantments.put("Unbreaking", 3);
    }
    public Inventory getMainInventory(){
        return this.mainInventory;
    }
    public Inventory getMiningToolInventory(){
        return this.miningToolInventory;
    }
    public Inventory getMiningToolVanillaInventory(){
        return this.miningToolVanillaInventory;
    }
    public Inventory getMiningToolCustomInventory(){
        return this.miningToolCustomInventory;
    }
    public Inventory getAxeInventory(){
        return this.axeInventory;
    }
    public Inventory getAxeVanillaInventory(){
        return this.axeVanillaInventory;
    }
    public Inventory getAxeCustomInventory(){
        return this.axeCustomInventory;
    }
    public Inventory getMeleeInventory(){
        return this.meleeInventory;
    }
    public Inventory getMeleeVanillaInventory(){
        return this.meleeVanillaInventory;
    }
    public Inventory getMeleeCustomInventory(){
        return this.meleeCustomInventory;
    }
    public Inventory getLevelMarketInventory(){
        return this.levelMarketInventory;
    }
    private void putEnchants(HashMap<String,Integer> enchants, Inventory inventory,ItemStack displayItem,int itemIndex,int itemSpace){
        ItemMeta displayItemItemMeta = (EnchantmentStorageMeta) displayItem.getItemMeta();
        for (String enchant : enchants.keySet()){
            displayItemItemMeta.setDisplayName(enchant);
            String bukkitName = enchant.toUpperCase().replace(" ", "_");
            Enchantment enchantment = Enchantment.getByName(bukkitName);
            displayItemItemMeta.addEnchant(enchantment,enchants.get(enchant),true);
            displayItem.setItemMeta(displayItemItemMeta);
            inventory.setItem(itemIndex,displayItem);
            itemIndex = itemIndex + itemSpace;
            displayItemItemMeta.removeEnchant(enchantment);
        }

    }
    private ItemStack createBackButton() {
        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta meta = back.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Back");
        back.setItemMeta(meta);
        return back;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(!(commandSender instanceof Player)) return false;
        Player player = (Player) commandSender;
        //Enchantment Market Button
        ItemStack mainInventorybook = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta mainInventoryBookMeta = (EnchantmentStorageMeta)mainInventorybook.getItemMeta();
        mainInventoryBookMeta.addEnchant(Enchantment.UNBREAKING,3,true);
        mainInventoryBookMeta.setDisplayName(ChatColor.GOLD+"Level Market");
        mainInventorybook.setItemMeta(mainInventoryBookMeta);
        //Close Barrier Button
        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta barrierMeta = barrier.getItemMeta();
        barrierMeta.setDisplayName(ChatColor.RED+"Close Menu");
        barrier.setItemMeta(barrierMeta);
        //Main Inventory
        mainInventory.setItem(11,mainInventorybook);
        mainInventory.setItem(13,player.getInventory().getItemInMainHand());
        mainInventory.setItem(15,barrier);
        player.openInventory(mainInventory);
        player.setMetadata("openedMenu", new FixedMetadataValue(RpgTools.getPlugin(), "mainInventory"));

        //Level Market Inventory
        ItemStack levelMarketTools = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta levelMarketToolsMeta = levelMarketTools.getItemMeta();
        levelMarketToolsMeta.setDisplayName(ChatColor.GOLD+"Tool Level Market");
        levelMarketTools.setItemMeta(levelMarketToolsMeta);
        ItemStack levelMarketMelee = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta levelMarketMeleeMeta = levelMarketMelee.getItemMeta();
        ItemStack levelMarketAxe = new ItemStack(Material.DIAMOND_AXE);
        ItemMeta levelMarketAxeMeta = levelMarketAxe.getItemMeta();
        levelMarketAxeMeta.setDisplayName(ChatColor.GOLD+"Axe Level Market");
        levelMarketAxe.setItemMeta(levelMarketAxeMeta);
        levelMarketMeleeMeta.setDisplayName(ChatColor.GOLD+"Melee Weapon Level Market");
        levelMarketMelee.setItemMeta(levelMarketMeleeMeta);
        levelMarketInventory.setItem(11,levelMarketTools);
        levelMarketInventory.setItem(15,levelMarketAxe);
        levelMarketInventory.setItem(15,levelMarketMelee);



        //Mining Tool Inventory
        //Vanilla Book
        ItemStack miningToolBook =  new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta miningToolBookMeta = (EnchantmentStorageMeta) miningToolBook.getItemMeta();
        miningToolBookMeta.setDisplayName(ChatColor.WHITE+"Vanilla Enchantments");
        miningToolBookMeta.addEnchant(Enchantment.UNBREAKING,3,true);
        miningToolBook.setItemMeta(miningToolBookMeta);
        //Custom Enchant
        ItemStack miningToolDiamond = new ItemStack(Material.DIAMOND);
        ItemMeta miningToolDiamondMeta = miningToolDiamond.getItemMeta();
        miningToolDiamondMeta.setDisplayName(ChatColor.WHITE+"Custom Enchantments");
        miningToolDiamondMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        miningToolDiamond.setItemMeta(miningToolDiamondMeta);

        miningToolInventory.setItem(11,miningToolBook);
        miningToolInventory.setItem(13,player.getInventory().getItemInMainHand());
        miningToolInventory.setItem(15,miningToolDiamond);

        //Mining Tool Vanilla Inventory
        ItemStack miningToolVanillaBook = new ItemStack(Material.ENCHANTED_BOOK);
        putEnchants(toolEnchantments,miningToolVanillaInventory,miningToolVanillaBook,9,2);
        //Mining Tool Custom Inventory



        //Axe Inventory
        axeInventory.setItem(11,miningToolBook);
        axeInventory.setItem(13,player.getInventory().getItemInMainHand());
        axeInventory.setItem(15,miningToolDiamond);

        //Axe Vanilla Inventory
        ItemStack axeVanillaBook = new ItemStack(Material.ENCHANTED_BOOK);
        putEnchants(axeEnchantments,axeVanillaInventory,axeVanillaBook,0,2);

        //Axe Custom Inventory


        //Melee Inventory
        meleeInventory.setItem(11,miningToolBook);
        meleeInventory.setItem(13,player.getInventory().getItemInMainHand());
        meleeInventory.setItem(15,miningToolDiamond);


        //Melee Vanilla Inventory
        ItemStack meleeVanillaBook = new ItemStack(Material.ENCHANTED_BOOK);
        putEnchants(meleeEnchantments,meleeVanillaInventory,meleeVanillaBook,0,2);


        //Melee Custom Inventory


        //Back Button
        axeInventory.setItem(22, createBackButton());
        axeVanillaInventory.setItem(22, createBackButton());
        axeCustomInventory.setItem(22, createBackButton());

        meleeInventory.setItem(22, createBackButton());
        meleeVanillaInventory.setItem(22, createBackButton());
        meleeCustomInventory.setItem(22, createBackButton());

        miningToolInventory.setItem(22, createBackButton());
        miningToolVanillaInventory.setItem(22, createBackButton());
        miningToolCustomInventory.setItem(22, createBackButton());

        levelMarketInventory.setItem(22, createBackButton());
        return true;
    }
}
