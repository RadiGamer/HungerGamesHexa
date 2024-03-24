package org.hexa.hungergameshexa.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.hexa.hungergameshexa.HungerGamesHexa;
import org.hexa.hungergameshexa.tasks.StartCountdown;
import org.hexa.hungergameshexa.tasks.WorldBorder1;
import org.hexa.hungergameshexa.util.GameState;

public class GameManager {

    private GameState gameState = GameState.ESPERANDO;
    private final HungerGamesHexa plugin;
    private StartCountdown startCountdown;
    private WorldBorder1 worldBorder1;

    public GameManager(HungerGamesHexa plugin) {
        this.plugin = plugin;
    }


    //TODO remover broadcastMessage de los switch cases

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
                this.worldBorder1 = new WorldBorder1(this,990,10); //TODO DEFINIR VALORES
                this.worldBorder1.runTaskTimer(plugin, 0, 20); //TODO METER DELAY
                Bukkit.broadcastMessage(ChatColor.RED+"El borde se esta cerrando...");
                break;
        }
    }

    public GameState getGameState() {
        return gameState;
    }
}
