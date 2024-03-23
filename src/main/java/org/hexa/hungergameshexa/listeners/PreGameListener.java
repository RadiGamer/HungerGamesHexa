package org.hexa.hungergameshexa.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.hexa.hungergameshexa.manager.GameManager;
import org.hexa.hungergameshexa.manager.GameState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PreGameListener implements Listener {
    private GameManager gameManager;
    private final Map<UUID, Long> messageCooldowns = new HashMap<>();

    private static final long MESSAGE_COOLDOWN_MS = 2000;

    public PreGameListener(GameManager gameManager){
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if ((gameManager.getGameState() == GameState.ESPERANDO || gameManager.getGameState() == GameState.COMENZANDO) && !player.hasPermission("hexa.admin")) {
            if (hasMoved(event.getFrom(), event.getTo())) {
                event.setTo(event.getFrom());

                long currentTime = System.currentTimeMillis();
                long lastMessageTime = messageCooldowns.getOrDefault(player.getUniqueId(), 0L);
                if (currentTime - lastMessageTime > MESSAGE_COOLDOWN_MS) {
                    player.sendMessage(ChatColor.RED + "Aún no te puedes mover.");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10, 1);
                    messageCooldowns.put(player.getUniqueId(), currentTime);
                }
            }
        }
    }

    private boolean hasMoved(org.bukkit.Location from, org.bukkit.Location to) {
        // Check if the player has changed their X, Y, or Z coordinates significantly (ignoring slight movements like head rotation)
        // Note: This checks if the player has moved from one block to another, not just any slight movement
        return from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Player player = event.getPlayer();

        if(!player.hasPermission("hexa.admin")){
            event.setCancelled(true);
        }
    }
}
