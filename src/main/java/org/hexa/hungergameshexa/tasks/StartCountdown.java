package org.hexa.hungergameshexa.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.hexa.hungergameshexa.HungerGamesHexa;
import org.hexa.hungergameshexa.manager.GameManager;
import org.hexa.hungergameshexa.util.GameState;
import org.hexa.hungergameshexa.util.ChatUtil;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

public class StartCountdown extends BukkitRunnable {

    private GameManager gameManager;
    private final HungerGamesHexa plugin;
    private BossBar countdownBar;

    public StartCountdown(GameManager gameManager, HungerGamesHexa plugin){
        this.gameManager = gameManager;
        this.plugin = plugin;
        this.countdownBar = Bukkit.createBossBar("Empezando en 1:30", BarColor.PINK, BarStyle.SOLID);
        this.countdownBar.setVisible(false);
    }
    private int timeLeft = 90; //TODO Colocar valor final despues de testear y borrar el comentario




    @Override
    public void run() {

        int minutes = timeLeft / 60;
        int seconds = timeLeft % 60;
        String timeFormatted = String.format("%d:%02d", minutes, seconds);

        if (!countdownBar.isVisible()) {
            countdownBar.setVisible(true);
            Bukkit.getOnlinePlayers().forEach(player -> countdownBar.addPlayer(player));
        }
        countdownBar.setTitle(ChatColor.BOLD +"Empezando en: " + timeFormatted);
        countdownBar.setProgress(timeLeft / 90.0);

        String start10 = plugin.getConfig().getString("messages.start10");
        String start60 = plugin.getConfig().getString("messages.start60");
        String start90 = plugin.getConfig().getString("messages.start90");

        String start10Message = String.format(start10, timeLeft);


        timeLeft--;
        if(timeLeft<=0){
            Bukkit.getOnlinePlayers().forEach(player -> countdownBar.removePlayer(player));
            countdownBar.setVisible(false);
            cancel();
            gameManager.setGameState(GameState.ACTIVO);
            for(Player player : Bukkit.getOnlinePlayers()){
                player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1 , 0);
            }
            return;
        }
        if(timeLeft<10){
            Bukkit.broadcastMessage(ChatUtil.format(start10Message));
            for(Player player : Bukkit.getOnlinePlayers()){
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1 , 0);
            }
        }
        if(timeLeft==60){
            Bukkit.broadcastMessage(ChatUtil.format(start60));
            for(Player player : Bukkit.getOnlinePlayers()){
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1 , 0);
            }
        }
        if(timeLeft==90){
            Bukkit.broadcastMessage(ChatUtil.format(start90));
            for(Player player : Bukkit.getOnlinePlayers()){
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1 , 0);
            }
        }

//TODO AGREGAR QUE APARTIR DE 10 SEGUNDOS COMIENZA LA CUENTA ATRAS
    }
}
