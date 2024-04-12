package org.hexa.hungergameshexa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hexa.hungergameshexa.HungerGamesHexa;
import org.hexa.hungergameshexa.util.ChatUtil;

public class ModoTorneoCommand implements CommandExecutor {
    private final HungerGamesHexa plugin;

    public ModoTorneoCommand(HungerGamesHexa plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String errorConsola = plugin.getConfig().getString("messages.error-console");
        String errorPermiso = plugin.getConfig().getString("messages.error-permission");

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatUtil.format(errorConsola));
            return true;
        }

        if (!sender.hasPermission("hexa.admin")) {
            sender.sendMessage(ChatUtil.format(errorPermiso));
            return true;
        }

        plugin.setModoTorneoEnabled(!plugin.isModoTorneoEnabled());
        sender.sendMessage(ChatUtil.format("&dEl modo torneo esta " + (plugin.isModoTorneoEnabled() ? "habilitado" : "deshabilitado")));
        return true;
    }
}
