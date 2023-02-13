package me.danjono.inventoryrollback.config;

import com.nuclyon.technicallycoded.inventoryrollback.InventoryRollbackPlus;
import com.nuclyon.technicallycoded.inventoryrollback.nms.EnumNmsVersion;
import org.bukkit.Sound;

public class SoundData extends ConfigData {

	private InventoryRollbackPlus main;

	private static Sound teleport;
	private static boolean teleportEnabled;

	private static Sound inventoryRestored;
	private static boolean inventoryRestoreEnabled;

	public SoundData() {
		this.main = InventoryRollbackPlus.getInstance();
	}

	public void setSounds() {	    
	    //If sounds are invalid they will be disabled.
		try {
			setTeleport(Sound.ENTITY_ENDERMAN_TELEPORT);
		} catch (NoSuchFieldError e) {
            if (this.main.getVersion().isNoHigherThan(EnumNmsVersion.v1_8_R3)) {
                setTeleport(Sound.valueOf("ENDERMAN_TELEPORT"));
            } else if (this.main.getVersion().isWithin(EnumNmsVersion.v1_9_R1, EnumNmsVersion.v1_12_R1)) {
                setTeleport(Sound.valueOf("ENTITY_ENDERMEN_TELEPORT"));
            }
		}
		
		if (teleport != null)
		    setTeleportEnabled((boolean) getDefaultValue("sounds.teleport.enabled", true));
		
		try {
			setInvetoryRestored(Sound.ENTITY_ENDER_DRAGON_FLAP);
		} catch (NoSuchFieldError e) {
            if (this.main.getVersion().isNoHigherThan(EnumNmsVersion.v1_8_R3)) {
                setInvetoryRestored(Sound.valueOf("ENDERDRAGON_WINGS"));
            } else if (this.main.getVersion().isWithin(EnumNmsVersion.v1_9_R1, EnumNmsVersion.v1_12_R1)) {
                setInvetoryRestored(Sound.valueOf("ENTITY_ENDERDRAGON_FLAP"));
            }
		}
		
		if (inventoryRestored != null)
		    setInventoryRestoredEnabled((boolean) getDefaultValue("sounds.inventory.enabled", true));
			}
	
	public static void setTeleport(Sound value) {
        teleport = value;
    }
    
    public static void setTeleportEnabled(boolean value) {
        teleportEnabled = value;
    }
    
    public static void setInvetoryRestored(Sound value) {
        inventoryRestored = value;
    }
    
    public static void setInventoryRestoredEnabled(boolean value) {
        inventoryRestoreEnabled = value;
    }
    

	public static Sound getTeleport() {
	    return teleport;
	}
	
	public static boolean isTeleportEnabled() {
	    return teleportEnabled;
	}
	
	public static Sound getInventoryRestored() {
	    return inventoryRestored;
	}
	
	public static boolean isInventoryRestoreEnabled() {
	    return inventoryRestoreEnabled;
	}
	
}
