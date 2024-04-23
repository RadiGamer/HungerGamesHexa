package org.hexa.hungergameshexa.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.hexa.hungergameshexa.HungerGamesHexa;
import org.hexa.hungergameshexa.listeners.PlayerJoinListener;
import org.hexa.hungergameshexa.tasks.EndGameTask;
import org.hexa.hungergameshexa.tasks.RemoveBarrels;
import org.hexa.hungergameshexa.tasks.StartCountdown;
import org.hexa.hungergameshexa.tasks.Border;
import org.hexa.hungergameshexa.util.ChatUtil;
import org.hexa.hungergameshexa.util.GameState;

public class GameManager {

    private GameState gameState = GameState.ESPERANDO;
    private final HungerGamesHexa plugin;
    private StartCountdown startCountdown;
    private TimeManager timeManager;
    private Border border;
    private EndGameTask endGameTask;
    private PlayerManager playerManager;
    private ChestManager chestManager;
    private RemoveBarrels removeBarrels;
    private DropManager dropManager;
    private ChestTier2Manager chestTier2Manager;
    private PlayerJoinListener playerJoinListener;

    private boolean autoStart = true;

    public GameManager(HungerGamesHexa plugin, ChestManager chestManager, ChestTier2Manager chestTier2Manager, PlayerJoinListener playerJoinListener) {
        this.plugin = plugin;
        this.timeManager = new TimeManager(plugin, this);
        this.endGameTask = new EndGameTask(plugin, this);

        this.playerManager = new PlayerManager(plugin, this);
        this.chestManager = chestManager;
        this.removeBarrels = new RemoveBarrels(plugin);
        this.dropManager = new DropManager(plugin);
        this.chestTier2Manager = chestTier2Manager;
        this.playerJoinListener = playerJoinListener;
    }

    public void setGameState(GameState gameState) {

        String border300 = plugin.getConfig().getString("messages.border-300");
        String border150 = plugin.getConfig().getString("messages.border-150");
        String border50 = plugin.getConfig().getString("messages.border-50");


        this.gameState = gameState;
        switch (gameState) {

            case ESPERANDO:
                this.startCountdown.cancel();

                break;

            case COMENZANDO:
                //ADD HERE THE player.teleport(spawnLocation);

                this.startCountdown = new StartCountdown(this, plugin);
                this.startCountdown.runTaskTimer(plugin, 0, 20);

                break;
            case ACTIVO:
                for (Player player : Bukkit.getOnlinePlayers()) {
                    timeManager.getBossBar().addPlayer(player);
                }
                timeManager.startTimer();

                break;

            case BORDE1:
                Bukkit.broadcastMessage(ChatUtil.format(border300));
                Border.setBorder(300, 70);
                break;

            case BORDE2:
                chestManager.resetChests(true);
                Bukkit.broadcastMessage(ChatUtil.format(border150));
                dropManager.dropRandomlyInZone();
                Border.setBorder(150, 70);
                break;

            case BORDE3:
                Bukkit.broadcastMessage(ChatUtil.format(border50));
                Border.setBorder(50, 70);
                break;

            case GANADOR:
                endGameTask.winnerFirework();
                timeManager.stopTimer();

                break;

            case REINICIANDO:
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.kickPlayer(ChatColor.LIGHT_PURPLE + "Gracias por Jugar. " + ChatColor.WHITE + "El juego se esta reiniciando.");
                }
                resetChests();
                playerJoinListener.clearPlayerSpawnPoints();
                playerManager.gameStarted = false;
                playerManager.setStarted(false);
                timeManager.resetTimer();
                removeBarrels.removeBarrelsInArea("world_1", -340, 322, 373, -367);
                this.setGameState(GameState.ESPERANDO);
                Border.setBorder(550, 0);
                break;

        }//TODO DEFINIR VALORES BIEN
    }

    public GameState getGameState() {
        return gameState;
    }

    public void resetChests() {
        chestTier2Manager.resetChests(false);
        chestManager.resetChests(true);
    }

    public void toggleAutoStart() {
        autoStart = !autoStart;
    }

    public boolean isAutoStart() {
        return autoStart;
    }
}
