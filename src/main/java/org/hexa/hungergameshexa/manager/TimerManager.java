package org.hexa.hungergameshexa.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.hexa.hungergameshexa.HungerGamesHexa;

public class TimerManager {
    private BossBar bossBar;
    private int taskId;
    private int gameTime;
    private final HungerGamesHexa plugin;
    private GameManager gameManager;
    private DropLootManager dropLootManager;

    public TimerManager(HungerGamesHexa plugin) {
        this.plugin = plugin;
        bossBar = Bukkit.createBossBar("Tiempo Transcurrido", BarColor.PURPLE, BarStyle.SEGMENTED_20);
        bossBar.setVisible(true);
    }

    public void startTimer() {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {

            gameTime++;
            int minutos = gameTime / 60;
            int segundos = gameTime % 60;

            String timeString = String.format("%02d:%02d", minutos, segundos);
            bossBar.setTitle(ChatColor.BOLD + "Tiempo Transcurrido: " + timeString);
        }, 0L, 20L);
    }

    public void stopTimer() {
        Bukkit.getScheduler().cancelTask(taskId);
    }

    public BossBar getBossBar() {
        return bossBar;
    }
}
