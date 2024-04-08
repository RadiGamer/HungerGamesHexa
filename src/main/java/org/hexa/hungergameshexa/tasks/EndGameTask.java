package org.hexa.hungergameshexa.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.hexa.hungergameshexa.HungerGamesHexa;
import org.hexa.hungergameshexa.manager.GameManager;
import org.hexa.hungergameshexa.util.ChatUtil;
import org.hexa.hungergameshexa.util.GameState;

public class EndGameTask {

    private final HungerGamesHexa plugin;
    private GameManager gameManager;


    public EndGameTask(HungerGamesHexa hungerGamesHexa, GameManager gameManager) {
        this.plugin = hungerGamesHexa;
        this.gameManager = gameManager;
    }

    public void winnerFirework(){
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam("Jugadores");
        String playerName = team.getEntries().iterator().next();
        Player player = Bukkit.getPlayer(playerName);
        Location location = player.getLocation();

        new BukkitRunnable() {
            int timer = 200;

            public void run() {
                if (timer == 200) {
                    Bukkit.broadcastMessage(ChatUtil.format("&e&lEl ganador es &a&l" + playerName + "&e&l!" ));
                }
                if(timer == 0){
                    this.cancel();
                    gameManager.setGameState(GameState.REINICIANDO);
                }
                Firework fw = player.getWorld().spawn(location, Firework.class);
                FireworkMeta meta = fw.getFireworkMeta();
                meta.addEffect(FireworkEffect.builder().withColor(Color.FUCHSIA).withFlicker().build());
                meta.setPower(1);
                fw.setFireworkMeta(meta);
                timer -= 20;
            }
        }.runTaskTimer(plugin, 0, 20);
    }
}

