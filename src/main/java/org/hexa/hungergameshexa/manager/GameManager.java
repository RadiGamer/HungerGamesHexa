package org.hexa.hungergameshexa.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.hexa.hungergameshexa.HungerGamesHexa;
import org.hexa.hungergameshexa.tasks.StartCountdown;
import org.hexa.hungergameshexa.tasks.Border;
import org.hexa.hungergameshexa.util.GameState;

public class GameManager {

    private GameState gameState = GameState.ESPERANDO;
    private final HungerGamesHexa plugin;
    private StartCountdown startCountdown;
    private TimeManager timeManager;
    private Border border;

    public GameManager(HungerGamesHexa plugin) {
        this.plugin = plugin;
        this.timeManager = new TimeManager(plugin, this);
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        switch (gameState){

            case ESPERANDO:

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
                Border.setBorder(500,100);
                break;

            case BORDE2:
                Border.setBorder(300,100);
                break;

            case BORDE3:
                Border.setBorder(100,100);
                break;

            case GANADOR:

                timeManager.stopTimer();
                break;

            case REINICIANDO:
                Border.setBorder(700,100);
                for (Player player : Bukkit.getOnlinePlayers()){
                    player.kickPlayer(ChatColor.GOLD + "El juego se esta reiniciando");
                }

                break;

        }//TODO Faltan los demas GameStates
    }
    public GameState getGameState() {
        return gameState;
    }
}
