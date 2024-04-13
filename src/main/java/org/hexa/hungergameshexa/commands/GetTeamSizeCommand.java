package org.hexa.hungergameshexa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.hexa.hungergameshexa.HungerGamesHexa;
import org.hexa.hungergameshexa.util.ChatUtil;
import org.jetbrains.annotations.NotNull;

public class GetTeamSizeCommand implements CommandExecutor {

    private final HungerGamesHexa plugin;
    private Team jugadores;
    private Scoreboard scoreboard;

    public GetTeamSizeCommand(HungerGamesHexa plugin){
        this.plugin = plugin;
        this.scoreboard = plugin.getServer().getScoreboardManager().getMainScoreboard();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        String errorPermiso = plugin.getConfig().getString("messages.error-permission");

        if (!commandSender.hasPermission("hexa.admin")) {
            commandSender.sendMessage(ChatUtil.format(errorPermiso));
            return true;
        }

        jugadores = scoreboard.getTeam("Jugadores");

        commandSender.sendMessage(ChatUtil.format("La cantidad de gente en el equipo es " + jugadores.getSize()));

        return false;
    }
}
