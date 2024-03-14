package org.hexa.hungergameshexa.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.hexa.hungergameshexa.manager.SpawnPointManager;

public class PlayerQuitListener implements Listener {

    private final SpawnPointManager spawnPointManager;

    public PlayerQuitListener(SpawnPointManager spawnPointManager) {
        this.spawnPointManager = spawnPointManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        spawnPointManager.handlePlayerQuit(event.getPlayer());
    }
}
