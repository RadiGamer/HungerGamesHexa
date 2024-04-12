package org.hexa.hungergameshexa.manager;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
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
    private TimeManager timeManager;
    public boolean gameStarted;
    private boolean countDownStarted;



//TODO AGREGAR QUE NO SE CUENTE A LA GENTE CON EL PERMISO DE HEXA.ADMIN
    public PlayerManager(HungerGamesHexa plugin, GameManager gameManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
        this.timeManager = new TimeManager(plugin, gameManager);
        setupTeamJugadores();
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            if (timeManager.isGracePeriodActive()) {
                event.setCancelled(true);
            }
        }
    }



    private void setupTeamJugadores(){
        Scoreboard scoreboard = plugin.getServer().getScoreboardManager().getMainScoreboard();
        jugadores = scoreboard.getTeam("Jugadores");
        if(jugadores == null){
            jugadores = scoreboard.registerNewTeam("Jugadores");
        }
    }

    private void checkAndUpdateGameState() {
        int playerCount = jugadores.getEntries().size();
        GameState currentState = gameManager.getGameState();

        if (playerCount >= 8 && currentState != GameState.COMENZANDO && !gameStarted && !countDownStarted) {
            gameManager.setGameState(GameState.COMENZANDO);
            countDownStarted = true;
        } if (playerCount < 8 && currentState != GameState.ESPERANDO && !gameStarted && countDownStarted) {
            gameManager.setGameState(GameState.ESPERANDO);
            countDownStarted=false;
        }if(!(currentState==GameState.ESPERANDO || currentState==GameState.COMENZANDO || currentState ==GameState.GANADOR)){
            gameStarted=true;
        }if (!(currentState == GameState.ESPERANDO || currentState == GameState.COMENZANDO) && playerCount == 1) {
            gameManager.setGameState(GameState.GANADOR);
        }

    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){



        String playerJoin = plugin.getConfig().getString("messages.player-join");
        String adminJoin = plugin.getConfig().getString("messages.admin-join");
        String gameStartedString = plugin.getConfig().getString("messages.game-started");

        Player player = event.getPlayer();
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20);
        player.setFoodLevel(20);


        if (player.hasPermission("hexa.admin")) {
            String adminJoinMessage = String.format(adminJoin, player.getName());
            event.setJoinMessage(ChatUtil.format(adminJoinMessage));
        } else {
            if (gameStarted) {
                if (!jugadores.hasEntry(player.getName()) && !player.isOp()) {
                    player.kickPlayer(gameStartedString); //TODO AsyncPlayerPreLoginEvent
                }
            } else {
                jugadores.addEntry(player.getName());
                String playerJoinMessage = String.format(playerJoin, player.getName(), jugadores.getEntries().size());
                event.setJoinMessage(ChatUtil.format(playerJoinMessage));
                player.getInventory().clear();
            }
        }


        checkAndUpdateGameState();
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        String adminQuit = plugin.getConfig().getString("messages.admin-quit");
        String playerQuit = plugin.getConfig().getString("messages.player-quit");
        String playerQuitMessage = String.format(playerQuit, event.getPlayer().getName(), jugadores.getEntries().size());
        String adminQuitMessage = String.format(adminQuit, event.getPlayer().getName());

        Player player = event.getPlayer();
        if (!player.hasPermission("hexa.admin")) {
            jugadores.removeEntry(player.getName());
            event.setQuitMessage(ChatUtil.format(playerQuitMessage));
        }else{
            event.setQuitMessage(ChatUtil.format(adminQuitMessage));
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this::checkAndUpdateGameState, 1L);

    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){

        Player player = event.getPlayer();
        jugadores.removeEntry(player.getName());

        String playerDeath = plugin.getConfig().getString("messages.player-death");
        String playerDeathMessage = String.format(playerDeath, event.getPlayer().getName(), jugadores.getEntries().size());

        event.setDeathMessage(ChatUtil.format(playerDeathMessage));

        player.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            player.spigot().respawn();
            player.setGameMode(GameMode.SPECTATOR);
            checkAndUpdateGameState();
        }, 1L);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this::checkAndUpdateGameState, 1L);
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }
}

