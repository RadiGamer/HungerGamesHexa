package org.hexa.hungergameshexa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hexa.hungergameshexa.HungerGamesHexa;
import org.hexa.hungergameshexa.util.ChatUtil;
import org.hexa.hungergameshexa.util.DropUtil;
import org.jetbrains.annotations.NotNull;

public class DropCommand implements CommandExecutor {

    private final HungerGamesHexa plugin;
    String errorConsola = "&c&lNo puedes usar este comando en la consola crack";
    String errorPermiso = "&c&lNo tienes permiso para ejecutar este comando";

    public DropCommand(HungerGamesHexa plugin) {
        this.plugin = plugin;
    }



    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(ChatUtil.format(errorConsola));
                return true;
            }

            if (!commandSender.hasPermission("hexa.admin")) {
                commandSender.sendMessage(ChatUtil.format(errorPermiso));
                return true;
            }

            DropUtil.dropBarrelRandomly(plugin);
            commandSender.sendMessage("Un Drop ha sido soltado en alguna parte del mundo!");
            return true;
        }
    //TODO COMANDO PARA TESTEO, BORRAR O MODIFICAR COMO SE DESEE
}
