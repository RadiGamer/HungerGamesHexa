package org.hexa.hungergameshexa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
        gameManager.setGameState(GameState.COMENZANDO);
        return false;
    }
}
