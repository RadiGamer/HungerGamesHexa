package org.hexa.hungergameshexa.tasks;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.hexa.hungergameshexa.manager.GameManager;
import org.hexa.hungergameshexa.util.GameState;

public class WorldBorder1 extends BukkitRunnable {

    private World world = Bukkit.getWorld("world");
    private double initialSize;
    private double targetSize;
    private double duration;
    private double currentTime = 0;

    private GameManager gameManager;

    public WorldBorder1(GameManager gameManager, double targetSize, int duration) {
        this.targetSize = targetSize;
        this.initialSize = world.getWorldBorder().getSize();
        this.duration = duration;
        this.gameManager = gameManager;
    }


    @Override
    public void run() {
        if(currentTime>=duration){
            gameManager.setGameState(GameState.BORDE1);
            cancel();
            return;
        }

        world.getWorldBorder().setSize(targetSize,10);

        // Reduce la duración en un segundo
        currentTime++;

        // Puedes imprimir mensajes o realizar otras acciones aquí si lo deseas
    }
}
