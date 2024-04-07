package org.hexa.hungergameshexa.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.hexa.hungergameshexa.HungerGamesHexa;
import org.hexa.hungergameshexa.util.ChatUtil;
import org.hexa.hungergameshexa.util.GameState;

public class TimeManager {
    private BossBar bossBar;
    private int taskId;
    private int gameTime;
    private final HungerGamesHexa plugin;
    private GameManager gameManager;

    public TimeManager(HungerGamesHexa plugin, GameManager gameManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
        bossBar = Bukkit.createBossBar(ChatColor.BOLD + "Esperando...", BarColor.YELLOW, BarStyle.SOLID);
    }

    public void startTimer() {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {

            World world = Bukkit.getWorld("world_1");
            bossBar.setVisible(true);
            gameTime++;
            int minutos = gameTime / 60;
            int segundos = gameTime % 60;

            String timeString = String.format("%02d:%02d", minutos, segundos);
            bossBar.setColor(BarColor.PURPLE);
            bossBar.setTitle(ChatColor.BOLD + "Tiempo Transcurrido: " + timeString + " | Borde Actual " + (int) world.getWorldBorder().getSize());



            if (minutos == 4 && segundos == 30) {
                Bukkit.broadcastMessage(ChatUtil.format("&c&lEl borde se cerrará en 30 segundos"));
                for (Player player : Bukkit.getOnlinePlayers()){
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0);
                }
            }
            if (minutos == 7 && segundos == 30) {
                Bukkit.broadcastMessage(ChatUtil.format("&c&lEl borde se cerrará en 30 segundos"));
                for (Player player : Bukkit.getOnlinePlayers()){
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0);
                }
            }
            if (minutos == 10 && segundos == 0) {
                Bukkit.broadcastMessage(ChatUtil.format("&c&lEl ultimo borde se cerrará en 30 segundos"));
                for (Player player : Bukkit.getOnlinePlayers()){
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0);
                }
            }
            //TODO AJUSTAR TIEMPOS ENTRE CIRCULOS Y DE CERRADO

            if (minutos == 5 && segundos == 0) {
                gameManager.setGameState(GameState.BORDE1);
            }
            if (minutos == 8 && segundos == 0) {
                gameManager.setGameState(GameState.BORDE2);
            }
            if (minutos == 10 && segundos == 30) {
                gameManager.setGameState(GameState.BORDE3);
            }

        }, 0L, 20L);
    }

    public void stopTimer() {
        Bukkit.getScheduler().cancelTask(taskId);
    }

    public BossBar getBossBar() {
        return bossBar;
    }
}
