package org.hexa.hungergameshexa.manager;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.hexa.hungergameshexa.HungerGamesHexa;
import org.hexa.hungergameshexa.util.ChatUtil;
import org.hexa.hungergameshexa.util.GameState;

public class PlayerManager implements Listener {
    private final HungerGamesHexa plugin;
    private Team jugadores;
    private final GameManager gameManager;

    public PlayerManager(HungerGamesHexa plugin, GameManager gameManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
        setupTeamJugadores();
    }
    private void setupTeamJugadores(){
        Scoreboard scoreboard = plugin.getServer().getScoreboardManager().getMainScoreboard();
        jugadores = scoreboard.getTeam("Jugadores");
        if(jugadores == null){
            jugadores = scoreboard.registerNewTeam("Jugadores");
        }
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        jugadores.addEntry(event.getPlayer().getName());
        event.setJoinMessage(ChatUtil.format("&9"+event.getPlayer().getName() + "&7 se ha unido al juego &3" + jugadores.getEntries().size() + "/16"));
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        jugadores.removeEntry(event.getPlayer().getName());
    }

    public void checkPlayers() {
        if(!(gameManager.getGameState() == GameState.ESPERANDO || gameManager.getGameState() == GameState.COMENZANDO)) {
            Bukkit.getScheduler().runTask(plugin, () -> {
                int teamSize = jugadores.getEntries().size();
                if (teamSize >= 8) {
                    gameManager.setGameState(GameState.COMENZANDO);
                } else if (teamSize < 7) {
                    gameManager.setGameState(GameState.ESPERANDO);
                }
                if((gameManager.getGameState() == GameState.BORDE1 || gameManager.getGameState() == GameState.BORDE2 || gameManager.getGameState() == GameState.BORDE3) && teamSize==1){
                    gameManager.setGameState(GameState.GANADOR);
                }
            });
        }
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player player = event.getPlayer();
        jugadores.removeEntry(player.getName());

        event.setDeathMessage(ChatUtil.format(player.getName()+ " &7Ha sido eliminado. &6Quedan " + jugadores.getEntries().size()+ " jugadores"));

        player.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            player.spigot().respawn();
            player.setGameMode(GameMode.SPECTATOR);
        }, 1L);

    }
}

