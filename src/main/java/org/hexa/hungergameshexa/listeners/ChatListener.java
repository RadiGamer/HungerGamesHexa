package org.hexa.hungergameshexa.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (!player.isOp()) {
            event.setFormat(ChatColor.WHITE+"\uE002 " + player.getDisplayName() + ChatColor.DARK_GRAY + " • " + ChatColor.GRAY + event.getMessage());
        } else {
            event.setFormat(ChatColor.WHITE+"\uE003 " + player.getDisplayName() + ChatColor.DARK_GRAY + " • " + ChatColor.LIGHT_PURPLE + event.getMessage());
        }
    }
}