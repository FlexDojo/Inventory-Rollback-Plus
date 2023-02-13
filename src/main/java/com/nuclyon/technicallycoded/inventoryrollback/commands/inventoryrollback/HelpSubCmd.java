package com.nuclyon.technicallycoded.inventoryrollback.commands.inventoryrollback;

import com.nuclyon.technicallycoded.inventoryrollback.InventoryRollbackPlus;
import com.nuclyon.technicallycoded.inventoryrollback.commands.IRPCommand;
import me.danjono.inventoryrollback.config.MessageData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class HelpSubCmd extends IRPCommand {

    public HelpSubCmd(InventoryRollbackPlus mainIn) {
        super(mainIn);
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("inventoryrollbackplus.help")) {
            this.sendHelp(sender);
        } else {
            sender.sendMessage(MessageData.getPluginPrefix() + MessageData.getNoPermission());
        }
        return;
    }

    public void sendHelp(CommandSender sender) {
        sender.sendMessage(
                MessageData.getPluginPrefix() + ChatColor.AQUA + "by TechnicallyCoded & Villag3r_\n" +
                        ChatColor.GOLD + "  Comandi disponibili:\n" +
                        ChatColor.GOLD + "    /irp restore [player]" + ChatColor.GRAY + " - Apre i backups di [player]\n" +
                        ChatColor.GOLD + "    /irp forcebackup <all/player> [player]" + ChatColor.GRAY + " - Salva forzatamente un backup\n" +
                        ChatColor.GOLD + "    /irp enable" + ChatColor.GRAY + " - Abilita il plugin\n" +
                        ChatColor.GOLD + "    /irp disable" + ChatColor.GRAY + " - Disabilita il plugin\n" +
                        ChatColor.GOLD + "    /irp reload" + ChatColor.GRAY + " - Reloadda il plugin\n" +
                        ChatColor.GOLD + "    /irp help" + ChatColor.GRAY + " - Apre questo pannello\n" +
                        ChatColor.GOLD + "    /irp version" + ChatColor.GRAY + " - Mostra info sulla versione\n");
    }

}
