package org.hexa.hungergameshexa;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.hexa.hungergameshexa.commands.*;
import org.hexa.hungergameshexa.listeners.*;
import org.hexa.hungergameshexa.manager.*;
import org.hexa.hungergameshexa.util.ChatUtil;
import org.hexa.hungergameshexa.manager.DropManager;

public final class HungerGamesHexa extends JavaPlugin {
    private boolean brownDyeEnabled = false;
    private GameManager gameManager;
    private SpawnPointManager spawnPointManager;
    private ChestManager chestManager;
    private DropLootManager dropLootManager;
    private TimeManager timeManager;
    private PlayerManager playerManager;
    private DropManager dropManager;
    private ChestTier2Manager chestTier2Manager;
    private PlayerJoinListener playerJoinListener;

    @Override
    public void onEnable() {
        Scoreboard scoreboard = this.getServer().getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam("Jugadores");
        if (team != null) {
            for (String entry : team.getEntries()) {
                team.removeEntry(entry);
                Bukkit.getConsoleSender().sendMessage("Eliminado" + entry);
            }
        }

        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, () -> {
            for(Player player : Bukkit.getOnlinePlayers()){
                if(player.getGameMode()== GameMode.ADVENTURE){
                    player.sendActionBar("\uE001");
                }
            }
        }, 0L, 20L);

        Bukkit.getConsoleSender().sendMessage("§aActivado Survival Games");
        saveDefaultConfig();
        ChatUtil.loadPrefixFromConfig(this);

        //MANAGERS

        this.chestManager = new ChestManager(getConfig(), this);
        this.chestTier2Manager = new ChestTier2Manager(getConfig(), this);
        this.gameManager = new GameManager(this, chestManager, chestTier2Manager, playerJoinListener);
        this.spawnPointManager = new SpawnPointManager(this);
        this.dropLootManager = new DropLootManager(this, getConfig());
        this.timeManager = new TimeManager(this, gameManager);
        this.playerManager = new PlayerManager(this, gameManager);
        this.dropManager = new DropManager(this);
        this.playerJoinListener = new PlayerJoinListener(this, gameManager, playerManager);


        //LISTENERS
        getServer().getPluginManager().registerEvents(new PreGameListener(gameManager, this),this);
        getServer().getPluginManager().registerEvents(new LootConfigListener(this), this);
        getServer().getPluginManager().registerEvents(chestManager, this);
        getServer().getPluginManager().registerEvents(dropLootManager, this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this, gameManager, playerManager), this);
        getServer().getPluginManager().registerEvents(new DropLootConfigListener(this), this);
        getServer().getPluginManager().registerEvents(playerManager, this);
        getServer().getPluginManager().registerEvents(new ChatListener(),this);
        getServer().getPluginManager().registerEvents(chestTier2Manager, this);


        //COMANDOS
        getCommand("lootconfig").setExecutor(new LootConfigCommand(this));
        getCommand("start").setExecutor(new StartCommand(gameManager));
        getCommand("setspawn").setExecutor(new SetSpawnCommand(this, spawnPointManager));
        getCommand("reset").setExecutor(new ResetCommand(this, gameManager));
        getCommand("droploot").setExecutor(new DropLootConfigCommand(this));
        getCommand("drop").setExecutor(new DropCommand(this));
        getCommand("ganador").setExecutor(new GanadorCommand(this, gameManager));
        getCommand("esperando").setExecutor(new EsperandoCommand(gameManager));
        getCommand("setdropzone").setExecutor(new SetDropZoneCommand(this));
        getCommand("droplocation").setExecutor(new DropOnLocationCommand(this));
        getCommand("loott2config").setExecutor(new LootT2ConfigCommand(this));
        getCommand("instastart").setExecutor(new InstaStartCommand(gameManager));
        getCommand("modotorneo").setExecutor(new ModoTorneoCommand(this));
        getCommand("getteamsize").setExecutor(new GetTeamSizeCommand(this));
        getCommand("autostart").setExecutor(new autoStart(gameManager));
    }


    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§4Desactivando SurvivalGames");
        // Plugin shutdown logic
    }

    public boolean isModoTorneoEnabled() {
        return brownDyeEnabled;
    }

    public void setModoTorneoEnabled(boolean brownDyeEnabled) {
        this.brownDyeEnabled = brownDyeEnabled;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    //TODO QUE LOS JUGADORES NO PUEDAN ENTRAR SI YA EMPEZO, EL QUE CAMBIE EL GAMESTATE DEPENDIENDO DE LA CAPACIDAD (Confirmar), LOBBY, SISTEMA DE TICKETS HABILITADO
}
