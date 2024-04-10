package org.hexa.hungergameshexa.util;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.Random;

public class DropZone {
    int x1, z1, x2, z2;

    public DropZone(int x1, int z1, int x2, int z2) {
        this.x1 = Math.min(x1, x2);
        this.z1 = Math.min(z1, z2);
        this.x2 = Math.max(x1, x2);
        this.z2 = Math.max(z1, z2);
    }

    public Location getRandomLocation(World world) {
        Random random = new Random();
        double x = x1 + (x2 - x1) * random.nextDouble();
        double z = z1 + (z2 - z1) * random.nextDouble();
        int y = world.getHighestBlockYAt((int)x, (int)z) + 50; // This makes it spawn above the highest block.
        return new Location(world, x, y, z);
    }
}
