package org.hexa.hungergameshexa.manager;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.FallingBlock;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.hexa.hungergameshexa.HungerGamesHexa;
import org.hexa.hungergameshexa.util.ChatUtil;
import org.hexa.hungergameshexa.util.DropZone;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class DropManager {

    private HungerGamesHexa plugin;

    public DropManager(HungerGamesHexa plugin) {
        this.plugin = plugin;
    }

    public void dropRandomlyInZone() {
        List<DropZone> zones = loadAllZones();
        if (!zones.isEmpty()) {
            Random random = new Random();
            DropZone selectedZone = zones.get(random.nextInt(zones.size()));
            World world = Bukkit.getServer().getWorld("world_1");
            if (world != null) {
                Location dropLocation = selectedZone.getRandomLocation(world);
                dropBarrel(plugin, world, dropLocation);
            }
        } else {
            fallbackDrop(plugin);
        }
    }
    private static void fallbackDrop(HungerGamesHexa plugin) {
        World defaultWorld = Bukkit.getServer().getWorld("world_1");
        if (defaultWorld != null) {
            Location randomLocation = getRandomLocation(defaultWorld);
            if (randomLocation != null) {
                dropBarrel(plugin, defaultWorld, randomLocation);
            }
        }
    }
    public List<DropZone> loadAllZones() {
        List<DropZone> zones = new ArrayList<>();
        ConfigurationSection zonesSection = plugin.getConfig().getConfigurationSection("drop-zones");
        if (zonesSection != null) {
            for (String key : zonesSection.getKeys(false)) {
                List<Double> corner1 = zonesSection.getDoubleList(key + ".corner1");
                List<Double> corner2 = zonesSection.getDoubleList(key + ".corner2");
                if (corner1.size() == 2 && corner2.size() == 2) {
                    DropZone zone = new DropZone(corner1.get(0).intValue(), corner1.get(1).intValue(), corner2.get(0).intValue(), corner2.get(1).intValue());
                    zones.add(zone);
                }
            }
        }
        return zones;
    }

    private static Location getRandomLocation(World world) {
        Random random = new Random();
        double worldBorderSize = world.getWorldBorder().getSize() / 2;
        Location center = world.getWorldBorder().getCenter();
        double x = center.getX() + (random.nextDouble() * worldBorderSize * 2) - worldBorderSize;
        double z = center.getZ() + (random.nextDouble() * worldBorderSize * 2) - worldBorderSize;
        int y = world.getHighestBlockYAt((int) x, (int) z) + 50;


        Bukkit.getConsoleSender().sendMessage("Loaded zones: " + world + " " + x + " " +  y + " " + z);

        return new Location(world, x, y, z);
    }


    public static void dropBarrel(HungerGamesHexa plugin, World world, Location location) { //----------ESTE SI JALA CHIDO---------
        Block block = world.getHighestBlockAt(location);
        location.setY(block.getY() + 50);
        FallingBlock fallingBlock = world.spawnFallingBlock(location, Material.BARREL.createBlockData());
        fallingBlock.setDropItem(false);
        fallingBlock.setHurtEntities(false);
        fallingBlock.setVelocity(new Vector(0, -0.0001, 0));

        String barrelDrop = plugin.getConfig().getString("messages.drop-message");
        String barrelDropMessage = String.format(barrelDrop, location.getBlockX(), location.getBlockZ());

        Bukkit.broadcastMessage(ChatUtil.format(barrelDropMessage));

        new BukkitRunnable(){
            @Override
            public void run() {
                if(!fallingBlock.isValid() || fallingBlock.isOnGround()){
                    world.playSound(fallingBlock.getLocation(), Sound.BLOCK_BARREL_OPEN, 1.0f, 1.0f);
                    world.spawnParticle(Particle.EXPLOSION_NORMAL, fallingBlock.getLocation(), 20, 0.5, 0.5, 0.5, 0.01);
                    world.strikeLightningEffect(fallingBlock.getLocation());

                    Particle.DustOptions pinkDustOptions = new Particle.DustOptions(Color.fromRGB(255, 105, 180), 3.0F);
                    new BukkitRunnable() {
                        int tiempo = 30 * 20;
                        int pasado = 0;

                        @Override
                        public void run() {
                            if (pasado >= tiempo) {
                                this.cancel();
                            } else {
                                for (int i = 0; i <= 50; i += 5) {
                                    world.spawnParticle(Particle.REDSTONE, fallingBlock.getLocation(), 30, 0.5, 30, 0.5, pinkDustOptions);
                                }
                                pasado += 5;
                            }
                        }
                    }.runTaskTimer(plugin, 0, 5);
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L,1L);
    }
}
