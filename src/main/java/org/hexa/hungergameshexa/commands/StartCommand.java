package org.hexa.hungergameshexa.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hexa.hungergameshexa.manager.GameManager;
import org.hexa.hungergameshexa.manager.GameState;
import org.jetbrains.annotations.NotNull;

public class StartCommand implements CommandExecutor {

    private GameManager gameManager;
    public StartCommand(GameManager gameManager){
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {


        if (!commandSender.hasPermission("hexa.admin")) {
            commandSender.sendMessage(ChatColor.RED + "No tienes permiso para ejecutar este comando.");
            return true;
        }

        gameManager.setGameState(GameState.COMENZANDO);
        return false;
    }
}
