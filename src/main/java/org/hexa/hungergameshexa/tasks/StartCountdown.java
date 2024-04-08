package org.hexa.hungergameshexa.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.hexa.hungergameshexa.manager.GameManager;
import org.hexa.hungergameshexa.util.GameState;
import org.hexa.hungergameshexa.util.ChatUtil;

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
            for(Player player : Bukkit.getOnlinePlayers()){
                player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1 , 0);
            }
            return;
        }
        Bukkit.broadcastMessage(ChatUtil.format("&7Empieza en &a"+timeLeft));
        for(Player player : Bukkit.getOnlinePlayers()){
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1 , 0);
        }
//TODO AGREGAR QUE APARTIR DE 10 SEGUNDOS COMIENZA LA CUENTA ATRAS
    }
}
