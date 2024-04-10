package org.hexa.hungergameshexa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hexa.hungergameshexa.HungerGamesHexa;
import org.hexa.hungergameshexa.util.ChatUtil;
import org.hexa.hungergameshexa.manager.DropManager;
import org.jetbrains.annotations.NotNull;

public class DropCommand implements CommandExecutor {

    private final HungerGamesHexa plugin;
    private DropManager dropManager;

    public DropCommand(HungerGamesHexa plugin) {
        this.plugin = plugin;
        this.dropManager = new DropManager(plugin);
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

            dropManager.dropRandomlyInZone();
            commandSender.sendMessage("Un Drop ha sido soltado en alguna parte del mundo!");
            return true;
        }
    //TODO COMANDO PARA TESTEO, BORRAR O MODIFICAR COMO SE DESEE
}
