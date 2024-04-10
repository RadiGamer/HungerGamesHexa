package org.hexa.hungergameshexa.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hexa.hungergameshexa.HungerGamesHexa;
import org.hexa.hungergameshexa.manager.ChestManager;
import org.hexa.hungergameshexa.manager.GameManager;
import org.hexa.hungergameshexa.util.ChatUtil;
import org.jetbrains.annotations.NotNull;

public class ResetCommand implements CommandExecutor {

    private final GameManager gameManager;
    private final HungerGamesHexa plugin;

    public ResetCommand(HungerGamesHexa plugin, GameManager gameManager) {
        this.gameManager = gameManager;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        String chestReset = plugin.getConfig().getString("messages.chest-reset");
        String errorPermiso = plugin.getConfig().getString("messages.error-permission");

        if (!commandSender.hasPermission("hexa.admin")) {
            commandSender.sendMessage(ChatUtil.format(errorPermiso));
            return true;
        }
        gameManager.resetChests();
            commandSender.sendMessage(ChatUtil.format(chestReset));
        return true;
    }
}
