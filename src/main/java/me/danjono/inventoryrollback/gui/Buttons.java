package me.danjono.inventoryrollback.gui;

import com.nuclyon.technicallycoded.inventoryrollback.InventoryRollbackPlus;
import com.nuclyon.technicallycoded.inventoryrollback.nms.EnumNmsVersion;
import me.danjono.inventoryrollback.config.MessageData;
import me.danjono.inventoryrollback.data.LogType;
import me.danjono.inventoryrollback.reflections.NBTWrapper;
import org.bukkit.*;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Buttons {

    private UUID uuid;

    private static final Material death = Material.BONE;

    private static final Material join =
            InventoryRollbackPlus.getInstance().getVersion().isAtLeast(EnumNmsVersion.v1_13_R1) ?
                    Material.OAK_SAPLING : Material.getMaterial("SAPLING");

    private static final Material quit =
            InventoryRollbackPlus.getInstance().getVersion().isAtLeast(EnumNmsVersion.v1_13_R1) ?
                    Material.RED_BED : Material.getMaterial("BED");

    private static final Material worldChange = Material.COMPASS;

    private static final Material forceSave = Material.DIAMOND;


    private static final Material pageSelector =
            InventoryRollbackPlus.getInstance().getVersion().isAtLeast(EnumNmsVersion.v1_13_R1) ?
                    Material.WHITE_BANNER : Material.getMaterial("BANNER");

    private static final Material teleport = Material.ENDER_PEARL;

    private static final Material enderChest = Material.ENDER_CHEST;

    private static final Material restoreAllInventory = Material.NETHER_STAR;

    private static final Material restoreAllInventoryDisabled = Material.REDSTONE_BLOCK;


    public Buttons(UUID uuid) {
        this.uuid = uuid;
    }
    
    public Buttons(OfflinePlayer player) {
        this.uuid = player.getUniqueId();
    }
    
    public static Material getDeathLogIcon() {
        return death;
    }
    
    public static Material getJoinLogIcon() {
        return join;
    }
    
    public static Material getQuitLogIcon() {
        return quit;
    }
    
    public static Material getWorldChangeLogIcon() {
        return worldChange;
    }
    
    public static Material getForceSaveLogIcon() {
        return forceSave;
    }

    public static Material getPageSelectorIcon() {
        return pageSelector;
    }

    public static Material getTeleportLocationIcon() {
        return teleport;
    }

    public static Material getEnderChestIcon() {
        return enderChest;
    }

    public static Material getRestoreAllInventoryIcon() {
        return restoreAllInventory;
    }

    public static Material getRestoreAllInventoryDisabledIcon() {
        return restoreAllInventoryDisabled;
    }

    public ItemStack nextButton(String displayName, LogType logType, int page, List<String> lore) {
        ItemStack button = new ItemStack(getPageSelectorIcon());
        BannerMeta meta = (BannerMeta) button.getItemMeta();

        List<Pattern> patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.BASE)); 
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.RHOMBUS_MIDDLE)); 
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.HALF_VERTICAL));
        patterns.add(new Pattern(DyeColor.GRAY, PatternType.BORDER));

        assert meta != null;
        meta.setPatterns(patterns);

        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);

        if (displayName != null) {
            meta.setDisplayName(displayName);
        }

        meta.setLore(lore);

        button.setItemMeta(meta);

        NBTWrapper nbt = new NBTWrapper(button);

        nbt.setString("uuid", uuid.toString());
        nbt.setString("logType", logType.name());
        nbt.setInt("page", page);
        button = nbt.setItemData();   

        return button;
    }

    public ItemStack backButton(String displayName, LogType logType, int page, List<String> lore) {
        ItemStack button = new ItemStack(getPageSelectorIcon());
        BannerMeta meta = (BannerMeta) button.getItemMeta();

        List<Pattern> patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.BASE));  
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.RHOMBUS_MIDDLE)); 
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.HALF_VERTICAL_MIRROR));
        patterns.add(new Pattern(DyeColor.GRAY, PatternType.BORDER));

        assert meta != null;
        meta.setPatterns(patterns);

        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);

        if (displayName != null) {
            meta.setDisplayName(displayName);
        }

        if (lore != null) {
            meta.setLore(lore);
        }

        button.setItemMeta(meta);

        NBTWrapper nbt = new NBTWrapper(button);

        nbt.setString("uuid", uuid.toString());
        nbt.setString("logType", logType.name());
        nbt.setInt("page", page);
        button = nbt.setItemData();

        return button;
    }

    public ItemStack enderChestNextButton(String displayName, LogType logType, int page, Long timestamp, List<String> lore) {
        ItemStack button = new ItemStack(getPageSelectorIcon());
        BannerMeta meta = (BannerMeta) button.getItemMeta();

        List<Pattern> patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.BASE));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.RHOMBUS_MIDDLE));
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.HALF_VERTICAL));
        patterns.add(new Pattern(DyeColor.GRAY, PatternType.BORDER));

        assert meta != null;
        meta.setPatterns(patterns);

        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);

        if (displayName != null) {
            meta.setDisplayName(displayName);
        }

        meta.setLore(lore);

        button.setItemMeta(meta);

        NBTWrapper nbt = new NBTWrapper(button);

        nbt.setString("uuid", uuid.toString());
        nbt.setString("logType", logType.name());
        nbt.setLong("timestamp", timestamp);
        nbt.setInt("page", page);
        button = nbt.setItemData();

        return button;
    }

    public ItemStack enderChestBackButton(String displayName, LogType logType, int page, Long timestamp, List<String> lore) {
        ItemStack button = new ItemStack(getPageSelectorIcon());
        BannerMeta meta = (BannerMeta) button.getItemMeta();

        List<Pattern> patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.BASE));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.RHOMBUS_MIDDLE));
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.HALF_VERTICAL_MIRROR));
        patterns.add(new Pattern(DyeColor.GRAY, PatternType.BORDER));

        assert meta != null;
        meta.setPatterns(patterns);

        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);

        if (displayName != null) {
            meta.setDisplayName(displayName);
        }

        if (lore != null) {
            meta.setLore(lore);
        }

        button.setItemMeta(meta);

        NBTWrapper nbt = new NBTWrapper(button);

        nbt.setString("uuid", uuid.toString());
        nbt.setString("logType", logType.name());
        nbt.setLong("timestamp", timestamp);
        nbt.setInt("page", page);
        button = nbt.setItemData();

        return button;
    }

    public ItemStack mainMenuBackButton(String displayName) {
        ItemStack button = new ItemStack(getPageSelectorIcon());
        BannerMeta meta = (BannerMeta) button.getItemMeta();

        List<Pattern> patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.BASE));  
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.RHOMBUS_MIDDLE)); 
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.HALF_VERTICAL_MIRROR));
        patterns.add(new Pattern(DyeColor.GRAY, PatternType.BORDER));

        assert meta != null;
        meta.setPatterns(patterns);

        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);

        if (displayName != null) {
            meta.setDisplayName(displayName);
        }

        button.setItemMeta(meta);

        NBTWrapper nbt = new NBTWrapper(button);

        nbt.setString("uuid", uuid.toString());
        button = nbt.setItemData();

        return button;
    }

    public ItemStack inventoryMenuBackButton(String displayName, LogType logType, Long timestamp) {
        ItemStack button = new ItemStack(getPageSelectorIcon());
        BannerMeta meta = (BannerMeta) button.getItemMeta();

        List<Pattern> patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.BASE)); 
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.RHOMBUS_MIDDLE)); 
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.HALF_VERTICAL_MIRROR));
        patterns.add(new Pattern(DyeColor.GRAY, PatternType.BORDER));

        assert meta != null;
        meta.setPatterns(patterns);

        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);

        if (displayName != null) {
            meta.setDisplayName(displayName);
        }

        button.setItemMeta(meta);

        NBTWrapper nbt = new NBTWrapper(button);

        nbt.setString("uuid", uuid.toString());
        nbt.setString("logType", logType.name());
        nbt.setLong("timestamp", timestamp);
        nbt.setInt("page", 0);

        button = nbt.setItemData();

        return button;
    }

    public ItemStack createInventoryButton(ItemStack item, LogType logType, String location, Long time, String displayName, List<String> lore) {    	
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        if (lore != null) {
            meta.setLore(lore);
        }

        meta.setDisplayName(displayName);

        item.setItemMeta(meta);

        NBTWrapper nbt = new NBTWrapper(item);

        nbt.setString("uuid", uuid.toString());
        nbt.setString("logType", logType.name());
        nbt.setLong("timestamp", time);
        nbt.setString("location", location);
        item = nbt.setItemData();

        return item;
    }

    public ItemStack createDeathLogButton(LogType logType, List<String> lore) {    	
        ItemStack item = new ItemStack(getDeathLogIcon());        
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        if (lore != null) {         
            meta.setLore(lore);
        }
        
        meta.setDisplayName(ChatColor.RED + "Morti");

        item.setItemMeta(meta);

        NBTWrapper nbt = new NBTWrapper(item);

        nbt.setString("uuid", uuid.toString());
        nbt.setString("logType", logType.name());
        item = nbt.setItemData();

        return item;
    }

    public ItemStack createJoinLogButton(LogType logType, List<String> lore) {      
        ItemStack item = new ItemStack(getJoinLogIcon());        
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        if (lore != null) {         
            meta.setLore(lore);
        }
        
        meta.setDisplayName(ChatColor.GREEN + "Connessione");

        item.setItemMeta(meta);

        NBTWrapper nbt = new NBTWrapper(item);

        nbt.setString("uuid", uuid.toString());
        nbt.setString("logType", logType.name());
        item = nbt.setItemData();

        return item;
    }

    public ItemStack createQuitLogButton(LogType logType, List<String> lore) {      
        ItemStack item = new ItemStack(getQuitLogIcon());        
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        if (lore != null) {         
            meta.setLore(lore);
        }
        
        meta.setDisplayName(ChatColor.GOLD + "Disconnessione");

        item.setItemMeta(meta);

        NBTWrapper nbt = new NBTWrapper(item);

        nbt.setString("uuid", uuid.toString());
        nbt.setString("logType", logType.name());
        item = nbt.setItemData();

        return item;
    }

    public ItemStack createWorldChangeLogButton(LogType logType, List<String> lore) {      
        ItemStack item = new ItemStack(getWorldChangeLogIcon());        
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        if (lore != null) {         
            meta.setLore(lore);
        }
        
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Cambio Mondo");

        item.setItemMeta(meta);

        NBTWrapper nbt = new NBTWrapper(item);

        nbt.setString("uuid", uuid.toString());
        nbt.setString("logType", logType.name());
        item = nbt.setItemData();

        return item;
    }

    public ItemStack createForceSaveLogButton(LogType logType, List<String> lore) {      
        ItemStack item = new ItemStack(getForceSaveLogIcon());        
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        if (lore != null) {         
            meta.setLore(lore);
        }
        
        meta.setDisplayName(ChatColor.AQUA + "Automatico");

        item.setItemMeta(meta);

        NBTWrapper nbt = new NBTWrapper(item);

        nbt.setString("uuid", uuid.toString());
        nbt.setString("logType", logType.name());
        item = nbt.setItemData();

        return item;
    }

    public ItemStack playerHead(List<String> lore, boolean setSkin) {    	
        if (uuid == null)
            return null;
        
        ItemStack skull = null;
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        
        if (InventoryRollbackPlus.getInstance().getVersion().isAtLeast(EnumNmsVersion.v1_13_R1)) {
            skull = new ItemStack(Material.PLAYER_HEAD);
        } else {
            Constructor<?> itemStackConstructor;
            
            try {
                itemStackConstructor = Class.forName("org.bukkit.inventory.ItemStack").getConstructor(Material.class, int.class, short.class);
                skull = (ItemStack) itemStackConstructor.newInstance(Material.getMaterial("SKULL_ITEM"), 1, (short) 3);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
        }

        assert skull != null;
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

        assert skullMeta != null;
        if (setSkin) {
            try {
                if (InventoryRollbackPlus.getInstance().getVersion().isAtLeast(EnumNmsVersion.v1_13_R1)) {
                    skullMeta.setOwningPlayer(player);
                } else {
                    Method method = skullMeta.getClass().getMethod("setOwner", String.class);
                    method.setAccessible(true);
                    method.invoke(skullMeta, player.getName());
                    method.setAccessible(false);
                }
            } catch (IllegalAccessException | IllegalArgumentException | SecurityException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }

            skullMeta.setDisplayName(ChatColor.RESET + player.getName());
        }

        if (lore != null) {	    	
            skullMeta.setLore(lore);
        }

        skull.setItemMeta(skullMeta);
        
        NBTWrapper nbt = new NBTWrapper(skull);
        
        nbt.setString("uuid", uuid + "");
        skull = nbt.setItemData();

        return skull;
    }

    public ItemStack enderPearlButton(LogType logType, String location) {    	
        ItemStack item = new ItemStack(getTeleportLocationIcon());

        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(MessageData.getDeathLocation());

        List<String> lore = new ArrayList<>();
        if (location != null) {
            String[] loc = location.split(",");
            lore.add(ChatColor.GOLD + "World: " + ChatColor.WHITE + loc[0]);
            lore.add(ChatColor.GOLD + "X: " + ChatColor.WHITE + loc[1]);
            lore.add(ChatColor.GOLD + "Y: " + ChatColor.WHITE + loc[2]);
            lore.add(ChatColor.GOLD + "Z: " + ChatColor.WHITE + loc[3]);

            meta.setLore(lore);
        } else {
            lore.add(ChatColor.WHITE + "Nessuna posizione");
        }

        item.setItemMeta(meta);

        NBTWrapper nbt = new NBTWrapper(item);

        nbt.setString("uuid", uuid.toString());
        nbt.setString("logType", logType.name());
        nbt.setString("location", location);
        item = nbt.setItemData();

        return item;
    }

    public ItemStack enderChestButton(LogType logType, Long timestamp, ItemStack[] enderChest) {    	
        ItemStack item = new ItemStack(getEnderChestIcon());

        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(MessageData.getEnderChestRestoreButton());

        List<String> lore = new ArrayList<>();

        if (enderChest.length > 1)
            lore.add(ChatColor.WHITE + "Oggetti in EnderChest");
        else {
            lore.add(ChatColor.WHITE + "Empty");
        }

        meta.setLore(lore);
        item.setItemMeta(meta);

        NBTWrapper nbt = new NBTWrapper(item);

        nbt.setString("uuid", uuid.toString());
        nbt.setString("logType", logType.name());
        nbt.setLong("timestamp", timestamp);
        item = nbt.setItemData();

        return item;
    }

    public ItemStack restoreAllInventory(LogType logType, Long timestamp) {       
        ItemStack item = new ItemStack(getRestoreAllInventoryIcon());

        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(MessageData.getMainInventoryRestoreButton());

        item.setItemMeta(meta);

        NBTWrapper nbt = new NBTWrapper(item);

        nbt.setString("uuid", uuid.toString());
        nbt.setString("logType", logType.name());
        nbt.setLong("timestamp", timestamp);
        item = nbt.setItemData();

        return item;
    }

    public ItemStack restoreAllInventoryDisabled(LogType logType, Long timestamp) {
        ItemStack item = new ItemStack(getRestoreAllInventoryDisabledIcon());

        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        String[] nameParts = MessageData.getMainInventoryDisabledButton().split("\\\\n");
        String titlePart = nameParts[0];
        ArrayList<String> loreParts = new ArrayList<>();

        meta.setDisplayName(titlePart);
        for (int i = 1; i < nameParts.length; i ++) {
            loreParts.add(nameParts[i]);
        }
        meta.setLore(loreParts);

        item.setItemMeta(meta);

        NBTWrapper nbt = new NBTWrapper(item);

        nbt.setString("uuid", uuid.toString());
        nbt.setString("logType", logType.name());
        nbt.setLong("timestamp", timestamp);
        item = nbt.setItemData();

        return item;
    }

}
