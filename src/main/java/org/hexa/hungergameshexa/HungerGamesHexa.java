package org.hexa.hungergameshexa;

import org.bukkit.plugin.java.JavaPlugin;
import org.hexa.hungergameshexa.commands.*;
import org.hexa.hungergameshexa.listeners.DropLootConfigListener;
import org.hexa.hungergameshexa.listeners.LootConfigListener;
import org.hexa.hungergameshexa.listeners.PlayerJoinListener;
import org.hexa.hungergameshexa.listeners.PreGameListener;
import org.hexa.hungergameshexa.manager.*;

public final class HungerGamesHexa extends JavaPlugin {
    private GameManager gameManager;
    private SpawnPointManager spawnPointManager;
    private ChestManager chestManager;
    private DropLootManager dropLootManager;
    private TimerManager timerManager;

//TODO NO OLVIDAR METER MULTIVERSE

    @Override
    public void onEnable() {
        saveDefaultConfig();


        //MANAGERS
        this.chestManager = new ChestManager(getConfig());
        this.gameManager = new GameManager(this);
        this.spawnPointManager = new SpawnPointManager(this);
        this.dropLootManager = new DropLootManager(getConfig());
        this.timerManager = new TimerManager(this);

        //LISTENERS
        getServer().getPluginManager().registerEvents(new PreGameListener(gameManager),this);
        getServer().getPluginManager().registerEvents(new LootConfigListener(this), this);
        getServer().getPluginManager().registerEvents(chestManager, this);
        getServer().getPluginManager().registerEvents(dropLootManager, this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new DropLootConfigListener(this), this);

        //COMANDOS
        getCommand("lootconfig").setExecutor(new LootConfigCommand(this));
        getCommand("start").setExecutor(new StartCommand(gameManager));
        getCommand("setspawn").setExecutor(new SetSpawnCommand(spawnPointManager));
        getCommand("reset").setExecutor(new ResetCommand(chestManager));
        getCommand("droploot").setExecutor(new DropLootConfigCommand(this));
        getCommand("drop").setExecutor(new DropCommand(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
