package org.hexa.hungergameshexa.tasks;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.hexa.hungergameshexa.HungerGamesHexa;
import org.hexa.hungergameshexa.manager.GameManager;
import org.hexa.hungergameshexa.util.ChatUtil;
import org.hexa.hungergameshexa.util.GameState;

import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class EndGameTask {

    private final HungerGamesHexa plugin;
    private GameManager gameManager;
    private BukkitRunnable victoryRunnable = null;

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

        String victory = plugin.getConfig().getString("messages.victory");
        String victoryMessage = String.format(victory, playerName);
        World world = getServer().getWorld("world_1");
        List<Entity> entityList = world.getEntities();

            if (victoryRunnable == null || victoryRunnable.isCancelled()) {
                victoryRunnable = new BukkitRunnable() {
                    int timer = 20*70; //60 DE Espera y 10 de Win announce

                    public void run() {

                        if (timer == 1400) {
                            Bukkit.broadcastMessage(ChatUtil.format(victoryMessage));
                        }
                        if (timer == 0) {
                            this.cancel();
                            gameManager.setGameState(GameState.REINICIANDO);

                            for (Entity current : entityList) {
                                if (current instanceof Item) {
                                    current.remove();
                                }
                            }
                        }
                        if(timer>1200) {
                            Firework fw = player.getWorld().spawn(location, Firework.class);
                            FireworkMeta meta = fw.getFireworkMeta();
                            meta.addEffect(FireworkEffect.builder().withColor(Color.FUCHSIA).withFlicker().build());
                            meta.setPower(1);
                            fw.setFireworkMeta(meta);
                        }
                        timer -= 20;
                    }
                };
                victoryRunnable.runTaskTimer(plugin, 0, 20);
            } else {
                Bukkit.getLogger().info("Ya se esta ejecutando el EndGameTask!");
            }
    }
}

