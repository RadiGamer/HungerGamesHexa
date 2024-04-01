package org.hexa.hungergameshexa.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.hexa.hungergameshexa.HungerGamesHexa;
import org.hexa.hungergameshexa.util.GameState;

public class TimerManager {
    private BossBar bossBar;
    private int taskId;
    private int gameTime;
    private final HungerGamesHexa plugin;
    private GameManager gameManager;
    private DropLootManager dropLootManager;

    public TimerManager(HungerGamesHexa plugin, GameManager gameManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
        bossBar = Bukkit.createBossBar("Tiempo Transcurrido", BarColor.PURPLE, BarStyle.SEGMENTED_20);
        bossBar.setVisible(true);

        for (Player player : Bukkit.getOnlinePlayers()){
            bossBar.addPlayer(player);
        }
    }

    public void startTimer() {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {

            World world = Bukkit.getWorld("world");

            gameTime++;
            int minutos = gameTime / 60;
            int segundos = gameTime % 60;

            String timeString = String.format("%02d:%02d", minutos, segundos);
            bossBar.setTitle(ChatColor.BOLD + "Tiempo Transcurrido: " + timeString + " | Borde Actual " + (int) world.getWorldBorder().getSize());



            if (minutos >= 5) {
                gameManager.setGameState(GameState.BORDE1);
            }
            if (minutos >= 7) {
                gameManager.setGameState(GameState.BORDE2);
            }
            if (minutos >= 8 && segundos >= 30) {
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
