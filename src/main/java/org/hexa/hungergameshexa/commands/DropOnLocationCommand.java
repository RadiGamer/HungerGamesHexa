package org.hexa.hungergameshexa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hexa.hungergameshexa.HungerGamesHexa;
import org.hexa.hungergameshexa.util.ChatUtil;
import org.hexa.hungergameshexa.manager.DropManager;
import org.jetbrains.annotations.NotNull;

public class DropOnLocationCommand implements CommandExecutor {

    private final HungerGamesHexa plugin;

    public DropOnLocationCommand(HungerGamesHexa plugin) {
        this.plugin = plugin;
    }



    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        String errorConsola = plugin.getConfig().getString("messages.error-console");
        String errorPermiso = plugin.getConfig().getString("messages.error-permission");

            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(ChatUtil.format(errorConsola));
                return true;
            }

            if (!commandSender.hasPermission("hexa.admin")) {
                commandSender.sendMessage(ChatUtil.format(errorPermiso));
                return true;
            }

            DropManager.dropBarrel(plugin, ((Player) commandSender).getWorld(), ((Player) commandSender).getLocation().add(0,50,0));

            return true;
        }
    //TODO COMANDO PARA TESTEO, BORRAR O MODIFICAR COMO SE DESEE
}
