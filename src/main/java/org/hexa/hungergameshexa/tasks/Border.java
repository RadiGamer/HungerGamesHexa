package org.hexa.hungergameshexa.tasks;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;

import java.util.concurrent.TimeUnit;

public class Border {

    public static void setBorder(int size, int time){
        World world = Bukkit.getWorld("world");
        WorldBorder worldBorder = world.getWorldBorder();

        worldBorder.setSize(size, TimeUnit.SECONDS, time);
    }

}