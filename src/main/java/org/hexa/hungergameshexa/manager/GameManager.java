package org.hexa.hungergameshexa.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.hexa.hungergameshexa.HungerGamesHexa;
import org.hexa.hungergameshexa.tasks.StartCountdown;
import org.hexa.hungergameshexa.tasks.WorldBorder1;
import org.hexa.hungergameshexa.util.GameState;

public class GameManager {

    private GameState gameState = GameState.ESPERANDO;
    private final HungerGamesHexa plugin;
    private StartCountdown startCountdown;
    private WorldBorder1 worldBorder1;
    private TimerManager timerManager;

    public GameManager(HungerGamesHexa plugin) {
        this.plugin = plugin;
        this.timerManager = new TimerManager(plugin);
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
                this.worldBorder1 = new WorldBorder1(this,500,10); //TODO DEFINIR VALORES
                this.worldBorder1.runTaskTimer(plugin, 0, 20); //TODO METER DELAY
                Bukkit.broadcastMessage(ChatColor.RED+"El borde se esta cerrando...");

                for(Player player : Bukkit.getOnlinePlayers()) {
                    timerManager.getBossBar().addPlayer(player);
                }
                timerManager.startTimer();

                break;

            case BORDE1:
                break;

            case BORDE2:
                break;

            case BORDE3:
                break;

            case GANADOR:

                timerManager.stopTimer();
                break;

            case REINICIANDO:
                this.worldBorder1 = new WorldBorder1(this, 700,1);
                this.worldBorder1.runTaskTimer(plugin,0, 20);
                break;

        }//TODO Faltan los demas GameStates
    }
// 700 BORDE ORIGINAL, 300 UN BORDE, 150 OTRO Y FINALMENTE 75
// 5 MINUTOS 1ER BORDE, 7 MINUTOS 2DO BORDE, 8:30 3ER BORDE, 10 ULTIMO BORDE)
    public GameState getGameState() {
        return gameState;
    }
}
