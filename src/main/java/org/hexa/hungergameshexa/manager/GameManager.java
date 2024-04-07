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

    public GameManager(HungerGamesHexa plugin) {
        this.plugin = plugin;
        this.timeManager = new TimeManager(plugin, this);
        this.endGameTask = new EndGameTask(plugin, this);
        this.playerManager = new PlayerManager(plugin,this);

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
                Border.setBorder(300,70);
                break;

            case BORDE2:
                DropUtil.dropBarrelRandomly(plugin);
                Border.setBorder(150,70);
                break;

            case BORDE3:
                Border.setBorder(50,70);
                break;

            case GANADOR:
                endGameTask.winnerFirework();
                timeManager.stopTimer();

                break;

            case REINICIANDO:
                Border.setBorder(550,0);
                Bukkit.broadcastMessage(ChatUtil.format("&oAqui va un kickall pare reiniciar todo con el texto pero no lo pongo por el desarrollo"));
                // for (Player player : Bukkit.getOnlinePlayers()){
                //    player.kickPlayer(ChatColor.GOLD + "El juego se esta reiniciando");
                //}

                break;

        }//TODO DEFINIR VALORES BIEN
    }
    public GameState getGameState() {
        return gameState;
    }
}
