package org.hexa.hungergameshexa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.hexa.hungergameshexa.manager.GameManager;
import org.hexa.hungergameshexa.util.ChatUtil;
import org.jetbrains.annotations.NotNull;

public class autoStart implements CommandExecutor {

    private GameManager gameManager;

    public autoStart(GameManager gameManager){
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        gameManager.toggleAutoStart();
        String status = gameManager.isAutoStart() ? "activado" : "desactivado";
        System.out.println("El auto-start esta " + status);
        commandSender.sendMessage(ChatUtil.format("El auto-start esta " + status));

        return false;
    }
}
