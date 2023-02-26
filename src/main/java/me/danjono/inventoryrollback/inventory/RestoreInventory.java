package me.danjono.inventoryrollback.inventory;

import com.nuclyon.technicallycoded.inventoryrollback.InventoryRollbackPlus;
import me.danjono.inventoryrollback.InventoryRollback;
import me.danjono.inventoryrollback.config.MessageData;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class RestoreInventory {

    private RestoreInventory() {
        throw new IllegalStateException("Restore inventory class");
    }

    public static ItemStack[] getInventoryItems(String packageVersion, String base64) {
        ItemStack[] inv = null;

        try {
            inv = stacksFromBase64(packageVersion, base64);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return inv;
    }

    private static ItemStack[] stacksFromBase64(String packageVersion, String data) {
        if (data == null)
            return new ItemStack[]{};

        ByteArrayInputStream inputStream = null;

        try {
            inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
        } catch (IllegalArgumentException e) {
            return new ItemStack[]{};
        }

        BukkitObjectInputStream dataInput = null;
        ItemStack[] stacks = null;

        try {
            dataInput = new BukkitObjectInputStream(inputStream);
            stacks = new ItemStack[dataInput.readInt()];
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        if (stacks == null)
            return new ItemStack[]{};

        for (int i = 0; i < stacks.length; i++) {
            try {
                stacks[i] = (ItemStack) dataInput.readObject();
            } catch (IOException | ClassNotFoundException | NullPointerException e) {
                //Backup generated before InventoryRollback v1.3
                if (packageVersion == null) {
                    InventoryRollbackPlus.getPluginLogger().severe(ChatColor.stripColor(MessageData.getPluginPrefix()) + "There was an error deserializing the material data. This is likely caused by a now incompatible material ID if the backup was originally generated on a different Minecraft server version.");
                }
                //Backup was not generated on the same server version
                else if (!packageVersion.equalsIgnoreCase(InventoryRollbackPlus.getPackageVersion())) {
                    InventoryRollbackPlus.getPluginLogger().severe(ChatColor.stripColor(MessageData.getPluginPrefix()) + "There was an error deserializing the material data. The backup was generated on a " + packageVersion + " version server whereas you are now running a " + InventoryRollback.getPackageVersion() + " version server. It is likely a material ID inside the backup is no longer valid on this Minecraft server version and cannot be convereted.");
                }
                //Unknown error
                else if (packageVersion.equalsIgnoreCase(InventoryRollbackPlus.getPackageVersion())) {
                    InventoryRollbackPlus.getPluginLogger().severe(ChatColor.stripColor(MessageData.getPluginPrefix()) + "There was an error deserializing the material data. The data file is likely corrupted since this was saved on the same version the server is currently running on so it should have worked.");
                }

                try { dataInput.close(); } catch (IOException e1) {}
                return null;
            }
        }

        try { dataInput.close(); } catch (IOException e1) {}

        return stacks;
    }
}
