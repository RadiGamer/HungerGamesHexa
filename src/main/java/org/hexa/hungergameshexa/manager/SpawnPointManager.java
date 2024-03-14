package org.hexa.hungergameshexa.manager;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
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
}
