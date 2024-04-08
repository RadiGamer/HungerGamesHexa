package org.hexa.hungergameshexa.manager;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.hexa.hungergameshexa.HungerGamesHexa;
import org.hexa.hungergameshexa.tasks.StartCountdown;
import org.hexa.hungergameshexa.util.ChatUtil;
import org.hexa.hungergameshexa.util.GameState;

public class PlayerManager implements Listener {
    private final HungerGamesHexa plugin;
    private Team jugadores;
    private final GameManager gameManager;
    private boolean isStarted;
    private int TaskId = -1;

//TODO AGREGAR QUE NO SE CUENTE A LA GENTE CON EL PERMISO DE HEXA.ADMIN
    public PlayerManager(HungerGamesHexa plugin, GameManager gameManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
        setupTeamJugadores();
        playerCheck();
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
        Player player = event.getPlayer();
        player.setHealth(20);
        player.setFoodLevel(20);
        if(!player.hasPermission("hexa.admin")) {
            jugadores.addEntry(player.getName());
            event.setJoinMessage(ChatUtil.format("&9" + player.getName() + "&7 se ha unido al juego &3" + jugadores.getEntries().size() + "/16"));
        }else{
            event.setJoinMessage(ChatUtil.format("&9" + player.getName() + "&7 se ha unido al juego &3"));
        }
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("hexa.admin")) {
            jugadores.removeEntry(player.getName());
            event.setQuitMessage(ChatUtil.format("&9" + player.getName() + "&7 ha abandonado el juego &3 " + jugadores.getEntries().size() + "/16"));
        }else{
            event.setQuitMessage(ChatUtil.format("&9" + player.getName() + "&7 ha abandonado el juego &3"));
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
    public void playerCheck(){
        GameState currentState = gameManager.getGameState();
        new BukkitRunnable(){
            @Override
            public void run(){
                if(jugadores.getEntries().size()>=8 && !isStarted && currentState == GameState.ESPERANDO){
                    gameManager.setGameState(GameState.COMENZANDO);
                    Bukkit.broadcastMessage(ChatUtil.format("a")); //TODO QUITAR ESTO
                    setStarted(true);
                }
                if (jugadores.getEntries().size()<=7 && isStarted && currentState == GameState.COMENZANDO) {
                    setStarted(false);
                    gameManager.setGameState(GameState.ESPERANDO);
                }
                if (jugadores.getEntries().size() == 1 && isStarted && (currentState == GameState.ACTIVO ||currentState == GameState.BORDE1 ||currentState == GameState.BORDE2 ||currentState == GameState.BORDE3)){
                    gameManager.setGameState(GameState.GANADOR);
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0 , 20);
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }
}

