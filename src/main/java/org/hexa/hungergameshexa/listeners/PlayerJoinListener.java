package org.hexa.hungergameshexa.listeners;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.hexa.hungergameshexa.HungerGamesHexa;
import org.hexa.hungergameshexa.manager.GameManager;
import org.hexa.hungergameshexa.manager.SpawnPointManager;
import org.hexa.hungergameshexa.util.ChatUtil;
import org.hexa.hungergameshexa.util.GameState;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerJoinListener implements Listener {


    private Map<UUID, Integer> playerSpawnMap = new HashMap<>();
    private Map<Integer, Boolean> spawnOccupied = new HashMap<>();
    private int maxSpawnpoints = 16;
    private final HungerGamesHexa plugin;
    private GameManager gameManager;


    public PlayerJoinListener(HungerGamesHexa plugin, GameManager gameManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        String serverFull = plugin.getConfig().getString("messages.server-full");
        String gameStarted = plugin.getConfig().getString("messages.game-started");

        Player player = event.getPlayer();
        player.setGameMode(GameMode.ADVENTURE);
        if (player.hasPermission("hexa.admin")) {
            return;
        }
        if(!(gameManager.getGameState()== GameState.ESPERANDO || gameManager.getGameState() == GameState.COMENZANDO)){
            player.kickPlayer(gameStarted);
        }

        int spawnNumber = assignSpawnpoint();
        if (spawnNumber == -1) {
            event.getPlayer().kickPlayer(ChatUtil.format(serverFull));
            return;
        }
        playerSpawnMap.put(player.getUniqueId(), spawnNumber);
        Location spawnLocation = getSpawnLocation(spawnNumber);
        player.teleport(spawnLocation);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("hexa.admin")) {
            return;
        }

        UUID playerId = player.getUniqueId();
        int assignedSpawn = playerSpawnMap.getOrDefault(playerId, -1);
        if (assignedSpawn != -1) {
            spawnOccupied.put(assignedSpawn, false);
            playerSpawnMap.remove(playerId);
        }
    }

    private int assignSpawnpoint() {
        for (int i = 1; i <= maxSpawnpoints; i++) {
            if (!spawnOccupied.getOrDefault(i, false)) {
                spawnOccupied.put(i, true);
                return i;
            }
        }
        return -1;
    }

    private Location getSpawnLocation(int spawnNumber) {
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();
        World world = Bukkit.getWorld("world_1");
        double x = config.getDouble("spawnpoints." + spawnNumber + ".x");
        double y = config.getDouble("spawnpoints." + spawnNumber + ".y");
        double z = config.getDouble("spawnpoints." + spawnNumber + ".z");
        float yaw = (float) config.getDouble("spawnpoints." + spawnNumber + ".yaw");
        float pitch = (float) config.getDouble("spawnpoints." + spawnNumber + ".pitch");

        return new Location(world, x, y, z, yaw, pitch);
    }


}
