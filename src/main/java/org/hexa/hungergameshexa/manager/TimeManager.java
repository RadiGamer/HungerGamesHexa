package org.hexa.hungergameshexa.manager;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
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
    private boolean gracePeriodActive;
    private DropManager dropManager;


    public TimeManager(HungerGamesHexa plugin, GameManager gameManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
        this.gracePeriodActive = false;
        bossBar = Bukkit.createBossBar(ChatColor.BOLD + "Esperando...", BarColor.YELLOW, BarStyle.SOLID);
        this.dropManager = new DropManager(plugin);
    }
    public boolean isGracePeriodActive() {
        return gracePeriodActive;
    }

    public void startTimer() {
        gracePeriodActive = true;

        String gracePeriodStartMessage = plugin.getConfig().getString("messages.grace-period-start", "&eEl tiempo de gracia ha comenzado. Los jugadores no pueden recibir daño por 30 segundos!");
        Bukkit.broadcastMessage(ChatUtil.format(gracePeriodStartMessage));


        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {

            String border30Sec = plugin.getConfig().getString("messages.border-30sec");


            World world = Bukkit.getWorld("world_1");
            bossBar.setVisible(true);
            gameTime++;
            int minutos = gameTime / 60;
            int segundos = gameTime % 60;

            String timeString = String.format("%02d:%02d", minutos, segundos);
            bossBar.setColor(BarColor.PURPLE);
            bossBar.setTitle(ChatColor.BOLD + "Tiempo Transcurrido: " + timeString + " | Borde Actual " + (int) world.getWorldBorder().getSize());

            if (gameTime == 30) {
                String gracePeriodEndMessage = plugin.getConfig().getString("messages.grace-period-end", "&cEl tiempo de gracia ha terminado. Cuidado!");
                Bukkit.broadcastMessage(ChatUtil.format(gracePeriodEndMessage));
                gracePeriodActive=false;
            }

            if (minutos == 6 && segundos == 30) {
                Bukkit.broadcastMessage(ChatUtil.format(border30Sec));
                for (Player player : Bukkit.getOnlinePlayers()){
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0);
                }
            }
            if (minutos == 9 && segundos == 30) {
                Bukkit.broadcastMessage(ChatUtil.format(border30Sec));
                for (Player player : Bukkit.getOnlinePlayers()){
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0);
                }
            }
            if (minutos == 14 && segundos == 30) {
                Bukkit.broadcastMessage(ChatUtil.format(border30Sec));
                for (Player player : Bukkit.getOnlinePlayers()){
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0);
                }
            }
            //TODO AJUSTAR TIEMPOS ENTRE CIRCULOS Y DE DROP

            if(minutos == 5 && segundos == 0){
                dropManager.dropRandomlyInZone();
            }
            if (minutos == 7 && segundos == 0) {
                gameManager.setGameState(GameState.BORDE1);
            }
            if(minutos == 9 && segundos == 0){
                dropManager.dropRandomlyInZone();
            }
            if (minutos == 10 && segundos == 0) {
                gameManager.setGameState(GameState.BORDE2);
            }
            if(minutos == 13 && segundos == 0){
                dropManager.dropRandomlyInZone();
                dropManager.dropRandomlyInZone();
            }
            if (minutos == 15 && segundos == 0) {
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

    public void resetTimer(){
        gameTime = 0;
    }
}
