package org.hexa.hungergameshexa.util;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.hexa.hungergameshexa.HungerGamesHexa;

import java.util.Random;

public class DropUtil {
    public static void dropBarrelRandomly(HungerGamesHexa plugin) {
                                        //(HungerGamesHexa plugin, World world)
        Location randomLocation = getRandomLocation(Bukkit.getWorld("world_1"));
        if (randomLocation != null) {
            dropBarrel(plugin, Bukkit.getWorld("world_1"), randomLocation);
        }
    }

    private static Location getRandomLocation(World world) {
        Random random = new Random();
        double worldBorderSize = world.getWorldBorder().getSize() / 2;
        Location center = world.getWorldBorder().getCenter();
        double x = center.getX() + (random.nextDouble() * worldBorderSize * 2) - worldBorderSize;
        double z = center.getZ() + (random.nextDouble() * worldBorderSize * 2) - worldBorderSize;
        int y = world.getHighestBlockYAt((int) x, (int) z) + 50; // 50 bloques arriba del punto mas alto

        return new Location(world, x, y, z);
    }

    private static void dropBarrel(HungerGamesHexa plugin, World world, Location location) {
        Block block = world.getHighestBlockAt(location);
        location.setY(block.getY() + 50);
        FallingBlock fallingBlock = world.spawnFallingBlock(location, Material.BARREL.createBlockData());
        fallingBlock.setDropItem(true);
        fallingBlock.setHurtEntities(false);
        fallingBlock.setVelocity(new Vector(0, -0.01, 0));

        new BukkitRunnable(){
            @Override
            public void run() {
                if(!fallingBlock.isValid() || fallingBlock.isOnGround()){
                    world.playSound(fallingBlock.getLocation(), Sound.BLOCK_BARREL_OPEN, 1.0f, 1.0f);
                    world.spawnParticle(Particle.EXPLOSION_NORMAL, fallingBlock.getLocation(), 20, 0.5, 0.5, 0.5, 0.01);
                    world.strikeLightningEffect(fallingBlock.getLocation());

                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L,1L);
    }
}
