package org.hexa.hungergameshexa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hexa.hungergameshexa.HungerGamesHexa;
import org.hexa.hungergameshexa.manager.GameManager;
import org.hexa.hungergameshexa.util.ChatUtil;
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

        String errorConsola = plugin.getConfig().getString("messages.error-console");
        String errorPermiso = plugin.getConfig().getString("messages.error-permission");


        if (!commandSender.hasPermission("hexa.admin")) {
            commandSender.sendMessage(ChatUtil.format(errorPermiso));
            return true;
        }
        gameManager.setGameState(GameState.GANADOR);

        return false;
    }
}//TODO ELIMINAR COMANDO
