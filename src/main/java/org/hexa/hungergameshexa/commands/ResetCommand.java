package org.hexa.hungergameshexa.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hexa.hungergameshexa.manager.ChestManager;
import org.jetbrains.annotations.NotNull;

public class ResetCommand implements CommandExecutor {

    private final ChestManager chestManager;


    public ResetCommand(ChestManager chestManager) {
        this.chestManager = chestManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!commandSender.hasPermission("hexa.admin")) {
            commandSender.sendMessage(ChatColor.RED + "No tienes permiso para ejecutar este comando.");
            return true;
        }

            chestManager.resetChests();
            commandSender.sendMessage("Cofres Reiniciados");
        return true;
    }
}
