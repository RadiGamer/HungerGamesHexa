package org.hexa.hungergameshexa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.hexa.hungergameshexa.HungerGamesHexa;
import org.hexa.hungergameshexa.manager.GameManager;
import org.hexa.hungergameshexa.util.GameState;
import org.jetbrains.annotations.NotNull;

public class GanadorCommand implements CommandExecutor {

    private final HungerGamesHexa plugin;
    private final GameManager gameManager;

    public GanadorCommand(HungerGamesHexa plugin, GameManager gameManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        gameManager.setGameState(GameState.GANADOR);

        return false;
    }
}//TODO ELIMINAR COMANDO
