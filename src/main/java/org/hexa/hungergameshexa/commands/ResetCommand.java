package org.hexa.hungergameshexa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.hexa.hungergameshexa.manager.ChestManager;
import org.jetbrains.annotations.NotNull;

public class ResetCommand implements CommandExecutor {

    private ChestManager chestManager;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(s.equalsIgnoreCase("reset")){
            chestManager.resetChests();
            commandSender.sendMessage("Cofres Reiniciados");
        }
        return true;
    }
}
