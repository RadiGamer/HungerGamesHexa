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
    private int timeLeft = 90; //TODO Colocar valor final despues de testear y borrar el comentario

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
        if(timeLeft<10){
            Bukkit.broadcastMessage(ChatUtil.format("&7La partida empieza en &a"+timeLeft));
            for(Player player : Bukkit.getOnlinePlayers()){
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1 , 0);
            }
        }
        if(timeLeft==60){
            Bukkit.broadcastMessage(ChatUtil.format("&7La partida empieza en &a1 &7minuto"));
            for(Player player : Bukkit.getOnlinePlayers()){
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1 , 0);
            }
        }
        if(timeLeft==90){
            Bukkit.broadcastMessage(ChatUtil.format("&7La partida empieza en &a1 &7minuto y &a30 &7segundos"));
            for(Player player : Bukkit.getOnlinePlayers()){
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1 , 0);
            }
        }

//TODO AGREGAR QUE APARTIR DE 10 SEGUNDOS COMIENZA LA CUENTA ATRAS
    }
}
