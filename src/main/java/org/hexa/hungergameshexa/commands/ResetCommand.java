package org.hexa.hungergameshexa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.hexa.hungergameshexa.manager.ChestManager;
import org.jetbrains.annotations.NotNull;

public class ResetCommand implements CommandExecutor {

    private final ChestManager chestManager;


    public ResetCommand(ChestManager chestManager) {
        this.chestManager = chestManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
            chestManager.resetChests();
            commandSender.sendMessage("Cofres Reiniciados");
        return true;
    }
}
