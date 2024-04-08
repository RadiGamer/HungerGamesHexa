package org.hexa.hungergameshexa.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.hexa.hungergameshexa.HungerGamesHexa;
import org.hexa.hungergameshexa.tasks.EndGameTask;
import org.hexa.hungergameshexa.tasks.StartCountdown;
import org.hexa.hungergameshexa.tasks.Border;
import org.hexa.hungergameshexa.util.ChatUtil;
import org.hexa.hungergameshexa.util.DropUtil;
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

    public GameManager(HungerGamesHexa plugin, ChestManager chestManager) {
        this.plugin = plugin;
        this.timeManager = new TimeManager(plugin, this);
        this.endGameTask = new EndGameTask(plugin, this);
        this.playerManager = new PlayerManager(plugin,this);
        this.chestManager = chestManager;

    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        switch (gameState){

            case ESPERANDO:
                this.startCountdown.cancel();
                playerManager.playerCheck();

                break;

            case COMENZANDO:
                this.startCountdown = new StartCountdown(this);
                this.startCountdown.runTaskTimer(plugin,0,20);
                break;
            case ACTIVO:
                for(Player player : Bukkit.getOnlinePlayers()) {
                    timeManager.getBossBar().addPlayer(player);
                }
                timeManager.startTimer();

                break;

            case BORDE1:
                Bukkit.broadcastMessage(ChatUtil.format("&cEl borde se esta cerrando &r| &6300 x 300"));
                Border.setBorder(300,70);
                break;

            case BORDE2:
                Bukkit.broadcastMessage(ChatUtil.format("&cEl borde se esta cerrando &r| &6150 x 150"));
                DropUtil.dropBarrelRandomly(plugin);
                Border.setBorder(150,70);
                break;

            case BORDE3:
                Bukkit.broadcastMessage(ChatUtil.format("&cEl borde se esta cerrando &r| &650 x 50"));
                Border.setBorder(50,70);
                break;

            case GANADOR:
                endGameTask.winnerFirework();
                timeManager.stopTimer();

                break;

            case REINICIANDO:
                Border.setBorder(550,0);
                // Bukkit.broadcastMessage(ChatUtil.format("&oYo habia ponido un kickall aqui.jpg"));
                chestManager.resetChests(false);
                playerManager.setStarted(false);
                 for (Player player : Bukkit.getOnlinePlayers()){
                    player.kickPlayer(ChatColor.GOLD + "El juego se esta reiniciando");
                }
                this.setGameState(GameState.ESPERANDO);
                break;

        }//TODO DEFINIR VALORES BIEN
    }
    public GameState getGameState() {
        return gameState;
    }
}
