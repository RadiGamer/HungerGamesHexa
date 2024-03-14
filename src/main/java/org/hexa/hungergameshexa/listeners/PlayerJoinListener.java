package org.hexa.hungergameshexa.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.hexa.hungergameshexa.manager.SpawnPointManager;

public class PlayerJoinListener implements Listener {

    private final SpawnPointManager spawnPointManager;

    public PlayerJoinListener(SpawnPointManager spawnPointManager) {
        this.spawnPointManager = spawnPointManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        spawnPointManager.handlePlayerJoin(event.getPlayer());
    }
}
