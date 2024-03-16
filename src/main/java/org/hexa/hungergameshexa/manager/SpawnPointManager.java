package org.hexa.hungergameshexa.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.hexa.hungergameshexa.HungerGamesHexa;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpawnPointManager {

    private final HungerGamesHexa plugin;
    private final Map<Integer, Location> spawnPoints = new HashMap<>();
    private final Map<UUID, Integer> playerSpawnPointMap = new HashMap<>();
    private final int maxPlayers = 16;

    public SpawnPointManager(HungerGamesHexa plugin) {
        this.plugin = plugin;
        loadSpawnPoints();
    }

    public boolean isFull() {
        return playerSpawnPointMap.size() >= maxPlayers;
    }

    public void handlePlayerJoin(Player player) {
        if (isFull()) {
            player.kickPlayer("Servidor lleno.");
            return;
        }

        int spawnPointIndex = findAvailableSpawnPoint();
        if (spawnPointIndex != -1) {
            player.teleport(spawnPoints.get(spawnPointIndex));
            playerSpawnPointMap.put(player.getUniqueId(), spawnPointIndex);
        }
    }

    public void handlePlayerQuit(Player player) {
        playerSpawnPointMap.remove(player.getUniqueId());
    }

    public void setSpawnPoint(Location location, int spawnPointIndex) {
        spawnPoints.put(spawnPointIndex, location);
    }

    private int findAvailableSpawnPoint() {
        for (int i = 1; i <= 16; i++) {
            if (!playerSpawnPointMap.containsValue(i) && spawnPoints.containsKey(i)) {
                return i;
            }
        }
        return -1;
    }
    public void loadSpawnPoints() {
        FileConfiguration config = plugin.getConfig();
        for (int i = 1; i <= maxPlayers; i++) {
            if (config.contains("spawnpoints." + i)) {
                String worldName = config.getString("spawnpoints." + i + ".world");
                double x = config.getDouble("spawnpoints." + i + ".x");
                double y = config.getDouble("spawnpoints." + i + ".y");
                double z = config.getDouble("spawnpoints." + i + ".z");
                float yaw = (float) config.getDouble("spawnpoints." + i + ".yaw");
                float pitch = (float) config.getDouble("spawnpoints." + i + ".pitch");
                Location loc = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
                spawnPoints.put(i, loc);
            }
        }
    }
    public void saveSpawnPoint(int index, Location location) {
        spawnPoints.put(index, location);
        String path = "spawnpoints." + index;
        FileConfiguration config = plugin.getConfig();
        config.set(path + ".world", location.getWorld().getName());
        config.set(path + ".x", location.getX());
        config.set(path + ".y", location.getY());
        config.set(path + ".z", location.getZ());
        config.set(path + ".yaw", location.getYaw());
        config.set(path + ".pitch", location.getPitch());
        plugin.saveConfig();
    }
}
