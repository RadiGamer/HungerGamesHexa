package org.hexa.hungergameshexa.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.hexa.hungergameshexa.commands.StartCommand;
import org.hexa.hungergameshexa.manager.GameManager;
import org.hexa.hungergameshexa.manager.GameState;

public class StartCountdown extends BukkitRunnable {

    private GameManager gameManager;

    public StartCountdown(GameManager gameManager){
        this.gameManager = gameManager;
    }
    private int timeLeft = 10; //TODO Colocar valor final despues de testear y borrar el comentario

    @Override
    public void run() {
        timeLeft--;
        if(timeLeft<=0){
            cancel();
            gameManager.setGameState(GameState.ACTIVO);
            return;
        }
        Bukkit.broadcastMessage(ChatColor.GRAY+"Empieza en "+ChatColor.GREEN +timeLeft);

    }
}
