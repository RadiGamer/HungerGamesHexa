package org.hexa.hungergameshexa;

import org.bukkit.plugin.java.JavaPlugin;
import org.hexa.hungergameshexa.commands.OpenGUICommand;
import org.hexa.hungergameshexa.commands.SetSpawnCommand;
import org.hexa.hungergameshexa.commands.StartCommand;
import org.hexa.hungergameshexa.listeners.InvListener;
import org.hexa.hungergameshexa.listeners.PlayerQuitListener;
import org.hexa.hungergameshexa.listeners.PreGameListener;
import org.hexa.hungergameshexa.manager.ChestManager;
import org.hexa.hungergameshexa.manager.GameManager;
import org.hexa.hungergameshexa.manager.SpawnPointManager;

public final class HungerGamesHexa extends JavaPlugin {
    private GameManager gameManager;
    private ChestManager chestManager;
    private SpawnPointManager spawnPointManager;

//TODO NO OLVIDAR METER MULTIVERSE

    @Override
    public void onEnable() {

        saveConfig();
        //MANAGERS
        this.gameManager = new GameManager(this);
        this.chestManager = new ChestManager(this);
        this.spawnPointManager = new SpawnPointManager(this);
        //LISTENERS
        getServer().getPluginManager().registerEvents(new InvListener(this,chestManager), this);
        getServer().getPluginManager().registerEvents(new PreGameListener(gameManager),this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(spawnPointManager), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(spawnPointManager), this);
        //COMANDOS
        getCommand("openinv").setExecutor(new OpenGUICommand(this));
        getCommand("start").setExecutor(new StartCommand(gameManager));
        getCommand("setspawn").setExecutor(new SetSpawnCommand(spawnPointManager));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
