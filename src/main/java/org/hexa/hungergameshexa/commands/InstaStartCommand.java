package org.hexa.hungergameshexa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.hexa.hungergameshexa.manager.GameManager;
import org.hexa.hungergameshexa.util.ChatUtil;
import org.hexa.hungergameshexa.util.GameState;
import org.jetbrains.annotations.NotNull;

public class InstaStartCommand implements CommandExecutor {

    private GameManager gameManager;
    String errorPermiso = "&c&lNo tienes permiso para ejecutar este comando";
    public InstaStartCommand(GameManager gameManager){
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {


        if (!commandSender.hasPermission("hexa.admin")) {
            commandSender.sendMessage(ChatUtil.format(errorPermiso));
            return true;
        }

        gameManager.setGameState(GameState.ACTIVO);
        return false;
    }
}
