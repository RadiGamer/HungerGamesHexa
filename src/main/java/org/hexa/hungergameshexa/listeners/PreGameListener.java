package org.hexa.hungergameshexa.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.hexa.hungergameshexa.manager.GameManager;
import org.hexa.hungergameshexa.manager.GameState;

public class PreGameListener implements Listener {
    private GameManager gameManager;

    public PreGameListener(GameManager gameManager){
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
       // if(!player.hasPermission("hexa.admin")) {
            if ((gameManager.getGameState() == GameState.ESPERANDO || gameManager.getGameState() == GameState.COMENZANDO) && !player.isOp()) {  //TODO QUITAR EL OP Y AGREGAR POR PERMISO
                event.setCancelled(true);
                player.sendMessage("Aun no te puedes mover"); //TODO REMOVER
            }
        // }
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if(gameManager.getGameState()== GameState.ESPERANDO || gameManager.getGameState()==GameState.COMENZANDO) { //TODO Crear BlockManager para romper ciertos bloques
            event.setCancelled(true);
        }
    }
}
