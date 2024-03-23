package org.hexa.hungergameshexa.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hexa.hungergameshexa.manager.ChestManager;
import org.hexa.hungergameshexa.util.ChatUtil;
import org.jetbrains.annotations.NotNull;

public class ResetCommand implements CommandExecutor {

    private final ChestManager chestManager;
    String errorPermiso = "&c&lNo tienes permiso para ejecutar este comando";
    String chestReset = "&eCofres Reiniciados";
    public ResetCommand(ChestManager chestManager) {
        this.chestManager = chestManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!commandSender.hasPermission("hexa.admin")) {
            commandSender.sendMessage(ChatUtil.format(errorPermiso));
            return true;
        }

            chestManager.resetChests();
            commandSender.sendMessage(ChatUtil.format(chestReset));
        return true;
    }
}
