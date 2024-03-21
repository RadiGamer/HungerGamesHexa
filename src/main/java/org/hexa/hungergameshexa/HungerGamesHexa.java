package org.hexa.hungergameshexa;

import org.bukkit.plugin.java.JavaPlugin;
import org.hexa.hungergameshexa.commands.LootConfigCommand;
import org.hexa.hungergameshexa.commands.ResetCommand;
import org.hexa.hungergameshexa.commands.SetSpawnCommand;
import org.hexa.hungergameshexa.commands.StartCommand;
import org.hexa.hungergameshexa.listeners.LootConfigListener;
import org.hexa.hungergameshexa.listeners.PreGameListener;
import org.hexa.hungergameshexa.manager.ChestManager;
import org.hexa.hungergameshexa.manager.GameManager;
import org.hexa.hungergameshexa.manager.SpawnPointManager;

public final class HungerGamesHexa extends JavaPlugin {
    private GameManager gameManager;
    private SpawnPointManager spawnPointManager;
    private ChestManager chestManager;

//TODO NO OLVIDAR METER MULTIVERSE

    @Override
    public void onEnable() {
        saveDefaultConfig();


        //MANAGERS
        this.chestManager = new ChestManager(getConfig());
        this.gameManager = new GameManager(this);
        this.spawnPointManager = new SpawnPointManager(this);
        //LISTENERS
        getServer().getPluginManager().registerEvents(new PreGameListener(gameManager),this);
        getServer().getPluginManager().registerEvents(new LootConfigListener(this), this);
        getServer().getPluginManager().registerEvents(chestManager, this);
        //COMANDOS
        getCommand("lootconfig").setExecutor(new LootConfigCommand(this));
        getCommand("start").setExecutor(new StartCommand(gameManager));
        getCommand("setspawn").setExecutor(new SetSpawnCommand(spawnPointManager));
        getCommand("reset").setExecutor(new ResetCommand(chestManager));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
