package org.hexa.hungergameshexa.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

public class Border {

    public static void setBorder(int size, int time){
        World world = Bukkit.getWorld("world");
        WorldBorder worldBorder = world.getWorldBorder();

        for(Player player : Bukkit.getOnlinePlayers()){
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1 ,0);
        }

        worldBorder.setSize(size, time);
    }

}
