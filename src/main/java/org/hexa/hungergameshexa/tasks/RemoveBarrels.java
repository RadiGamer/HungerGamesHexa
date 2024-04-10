package org.hexa.hungergameshexa.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.hexa.hungergameshexa.HungerGamesHexa;

public class RemoveBarrels {

    private HungerGamesHexa plugin;

    public RemoveBarrels(HungerGamesHexa plugin) {
        this.plugin = plugin;
    }

    public void removeBarrelsInArea(String worldName, int x1, int z1, int x2, int z2) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            plugin.getLogger().warning("World '" + worldName + "' not found!");
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                int minX = Math.min(x1, x2);
                int maxX = Math.max(x1, x2);
                int minZ = Math.min(z1, z2);
                int maxZ = Math.max(z1, z2);

                for (int x = minX; x <= maxX; x++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        for (int y = 0; y < world.getMaxHeight(); y++) {
                            if (world.getBlockAt(x, y, z).getType() == Material.BARREL) {
                                world.getBlockAt(x, y, z).setType(Material.AIR, false);
                            }
                        }
                    }
                }
            }
        }.runTaskAsynchronously(plugin);
    }
}