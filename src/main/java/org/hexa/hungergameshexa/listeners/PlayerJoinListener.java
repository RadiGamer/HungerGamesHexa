package org.hexa.hungergameshexa.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.hexa.hungergameshexa.HungerGamesHexa;
import org.hexa.hungergameshexa.manager.SpawnPointManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerJoinListener implements Listener {

    /* private final SpawnPointManager spawnPointManager;

    public PlayerJoinListener(SpawnPointManager spawnPointManager) {
        this.spawnPointManager = spawnPointManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        spawnPointManager.handlePlayerJoin(event.getPlayer());
    }
-------------------------
        private final HungerGamesHexa plugin;
        private final Map<UUID, Integer> playerSpawnPoints = new HashMap<>();
        private final boolean[] availableSpawns = new boolean[16]; // Assuming 16 spawn points
        private File playerDataFile;
        private FileConfiguration playerData;

        public PlayerJoinListener(HungerGamesHexa plugin) {
            this.plugin = plugin;
            // Load player data file
            playerDataFile = new File(plugin.getDataFolder(), "playerdata.yml");
            playerData = YamlConfiguration.loadConfiguration(playerDataFile);

            // Initially, all spawn points are available
            for (int i = 0; i < availableSpawns.length; i++) {
                availableSpawns[i] = true;
            }
        }

        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event) {
            Player player = event.getPlayer();
            // Check if player has the "hexa.admin" permission
            if (player.hasPermission("hexa.admin")) {
                return; // Do not assign spawn points to these players
            }

            Integer assignedSpawn = playerSpawnPoints.get(player.getUniqueId());
            if (assignedSpawn != null) {
                teleportToSpawn(player, assignedSpawn);
            } else {
                // Find an available spawn point
                assignedSpawn = findAvailableSpawn();
                if (assignedSpawn != -1) {
                    playerSpawnPoints.put(player.getUniqueId(), assignedSpawn);
                    teleportToSpawn(player, assignedSpawn);
                    savePlayerData(player.getUniqueId(), assignedSpawn);
                } else {
                    player.kickPlayer("All spawn points are currently occupied. Please try again later.");
                }
            }
        }

        private Integer findAvailableSpawn() {
            for (int i = 0; i < availableSpawns.length; i++) {
                if (availableSpawns[i]) {
                    availableSpawns[i] = false; // Mark as taken
                    return i;
                }
            }
            return -1; // No available spawn points
        }

        private void teleportToSpawn(Player player, int spawnIndex) {
            String path = "spawnpoints." + (spawnIndex + 1);
            FileConfiguration config = plugin.getConfig();
            World world = Bukkit.getWorld(config.getString(path + ".world"));
            double x = config.getDouble(path + ".x");
            double y = config.getDouble(path + ".y");
            double z = config.getDouble(path + ".z");
            float yaw = (float) config.getDouble(path + ".yaw");
            float pitch = (float) config.getDouble(path + ".pitch");

            Location spawnLocation = new Location(world, x, y, z, yaw, pitch);
            player.teleport(spawnLocation);
        }

        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event) {
            Player player = event.getPlayer();
            Integer spawnPointIndex = playerSpawnPoints.remove(player.getUniqueId());
            if (spawnPointIndex != null) {
                availableSpawns[spawnPointIndex] = true; // Mark as available again
                removePlayerData(player.getUniqueId());
            }
        }

        private void savePlayerData(UUID playerId, int spawnIndex) {
            playerData.set(playerId.toString(), spawnIndex);
            try {
                playerData.save(playerDataFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void removePlayerData(UUID playerId) {
            playerData.set(playerId.toString(), null);
            try {
                playerData.save(playerDataFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

    private Map<UUID, Integer> playerSpawnMap = new HashMap<>();
    private Map<Integer, Boolean> spawnOccupied = new HashMap<>();
    private int maxSpawnpoints = 16;
    private final HungerGamesHexa plugin;


    public PlayerJoinListener(HungerGamesHexa plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("hexa.admin")) {
            return;
        }

        int spawnNumber = assignSpawnpoint();
        if (spawnNumber == -1) {
            event.getPlayer().kickPlayer("El servidor esta lleno"); //TODO Dar Formato
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
            return; // Ignore players with admin permission
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
        // Assuming the spawnpoints are defined in the "world" world
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();
        World world = Bukkit.getWorld("world");
        double x = config.getDouble("spawnpoints." + spawnNumber + ".x");
        double y = config.getDouble("spawnpoints." + spawnNumber + ".y");
        double z = config.getDouble("spawnpoints." + spawnNumber + ".z");
        float yaw = (float) config.getDouble("spawnpoints." + spawnNumber + ".yaw");
        float pitch = (float) config.getDouble("spawnpoints." + spawnNumber + ".pitch");

        return new Location(world, x, y, z, yaw, pitch);
    }
}
