package org.hexa.hungergameshexa.listeners;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.hexa.hungergameshexa.HungerGamesHexa;
import org.hexa.hungergameshexa.manager.GameManager;
import org.hexa.hungergameshexa.manager.PlayerManager;
import org.hexa.hungergameshexa.manager.SpawnPointManager;
import org.hexa.hungergameshexa.util.ChatUtil;
import org.hexa.hungergameshexa.util.GameState;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class PlayerJoinListener implements Listener {


    private Map<UUID, Integer> playerSpawnMap = new HashMap<>();
    private Map<Integer, Boolean> spawnOccupied = new HashMap<>();
    private int maxSpawnpoints = 16;
    private final HungerGamesHexa plugin;
    private GameManager gameManager;
    private Team jugadores;
    private PlayerManager playerManager;
    private Map<UUID, Location> playerLocations = new HashMap<>();
    private Map<UUID, Integer> spawnPointAssignments = new HashMap<>();
    private int taskId = -1;


    public PlayerJoinListener(HungerGamesHexa plugin, GameManager gameManager, PlayerManager playerManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
        this.playerManager = playerManager;

        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        taskId = scheduler.scheduleSyncRepeatingTask(plugin, () -> {
            if (gameManager.getGameState() == GameState.COMENZANDO) {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    Location spawnLocation = playerLocations.get(player.getUniqueId());
                    if (spawnLocation != null) {
                        player.teleport(spawnLocation);
                        Bukkit.getLogger().info("Teletransportando " + player.getName() + " a su spawn.");
                    }
                }
                if (taskId != -1) {
                    scheduler.cancelTask(taskId);
                }
            }
        }, 0L, 20L);

    }
    public void clearPlayerSpawnPoints() {
        playerLocations.clear();
        Bukkit.getLogger().info("Todos los spawnpoints asignados han sido eliminados");
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        String playerJoin = plugin.getConfig().getString("messages.player-join");
        String adminJoin = plugin.getConfig().getString("messages.admin-join");

        World lobbyWorld = Bukkit.getWorld("lobby");
        Location lobbyLocation = new Location(lobbyWorld, 0, 58, 0);
        String gameStartedString = plugin.getConfig().getString("messages.game-started");

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (playerManager.gameStarted && jugadores != null && jugadores.hasEntry(player.getName())) {
                        this.cancel();
                    } else {
                        // Otherwise, teleport them to the lobby
                        player.teleport(lobbyLocation);
                    }
                }
            }.runTaskLater(plugin, 20);


        player.setGameMode(GameMode.ADVENTURE);

        Scoreboard scoreboard = plugin.getServer().getScoreboardManager().getMainScoreboard();
        jugadores = scoreboard.getTeam("Jugadores");
        if (jugadores != null && jugadores.hasEntry(player.getName())) {
            player.sendMessage(ChatUtil.format(plugin.getConfig().getString("messages.player-rejoin")));
        }

        if (player.hasPermission("hexa.admin")) {
            String adminJoinMessage = String.format(adminJoin, player.getName());
            event.setJoinMessage(ChatUtil.format(adminJoinMessage));
        } else {
            if (playerManager.gameStarted) {
                if (!jugadores.hasEntry(player.getName()) && !player.isOp()) {
                    player.kickPlayer(gameStartedString); //TODO AsyncPlayerPreLoginEvent
                }
            } else  {
                jugadores.addEntry(player.getName());
                String playerJoinMessage = String.format(playerJoin, player.getName(), jugadores.getEntries().size());
                event.setJoinMessage(ChatUtil.format(playerJoinMessage));
                player.getInventory().clear();
            }
        }

        if (!playerManager.gameStarted || !jugadores.hasEntry(player.getName())) {
            int spawnNumber = assignSpawnpoint(player.getUniqueId());
            if (spawnNumber == -1) {
                player.kickPlayer(ChatUtil.format(plugin.getConfig().getString("messages.server-full")));
                return;
            }
            Location spawnLocation = getSpawnLocation(spawnNumber);
            playerLocations.put(player.getUniqueId(), spawnLocation);
            Bukkit.getLogger().info("Asignado a " + player.getName() + ": " +
                    spawnLocation.getX() + ", " + spawnLocation.getY() + ", " + spawnLocation.getZ());

            if(gameManager.getGameState() == GameState.COMENZANDO){
                spawnLocation = getPlayerSpawnLocation(player.getUniqueId());
                if (spawnLocation != null) {
                    player.teleport(spawnLocation);
                }
            }
        }
    }
    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        UUID playerId = event.getUniqueId();
        String playerName = event.getName();

        if (!playerManager.gameStarted || !plugin.getServer().getScoreboardManager().getMainScoreboard().getTeam("Jugadores").hasEntry(playerName)) {
            int spawnNumber = assignSpawnpoint(playerId);
            if (spawnNumber == -1) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, ChatUtil.format(plugin.getConfig().getString("messages.server-full")));
            } else {
                spawnPointAssignments.put(playerId, spawnNumber);
                event.allow();
            }
        } else {
            event.allow();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        String adminQuit = plugin.getConfig().getString("messages.admin-quit");
        String playerQuit = plugin.getConfig().getString("messages.player-quit");
        String playerQuitMessage = String.format(playerQuit, event.getPlayer().getName(), jugadores.getEntries().size());
        String adminQuitMessage = String.format(adminQuit, event.getPlayer().getName());
        int assignedSpawn = playerSpawnMap.getOrDefault(playerId, -1);

        if (!player.hasPermission("hexa.admin")) {
            if(!playerManager.gameStarted) {
                jugadores.removeEntry(player.getName());
                event.setQuitMessage(ChatUtil.format(playerQuitMessage));
            }
        }else{
            event.setQuitMessage(ChatUtil.format(adminQuitMessage));
        }

        if (assignedSpawn != -1) {
            spawnOccupied.put(assignedSpawn, false);
            playerSpawnMap.remove(playerId);}

    }

    private synchronized int assignSpawnpoint(UUID playerId) {
        if (playerSpawnMap.containsKey(playerId)) {
            return playerSpawnMap.get(playerId);
        }

        for (int i = 1; i <= maxSpawnpoints; i++) {
            if (!spawnOccupied.getOrDefault(i, false)) {
                spawnOccupied.put(i, true);
                playerSpawnMap.put(playerId, i);
                Bukkit.getLogger().info("Asignado spawnpoint " + i);
                return i;
            }
        }
        return -1;
    }

    public Location getSpawnLocation(int spawnNumber) {
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();
        World world = Bukkit.getWorld("world_1");
        double x = config.getDouble("spawnpoints." + spawnNumber + ".x");
        double y = config.getDouble("spawnpoints." + spawnNumber + ".y");
        double z = config.getDouble("spawnpoints." + spawnNumber + ".z");
        float yaw = (float) config.getDouble("spawnpoints." + spawnNumber + ".yaw");
        float pitch = (float) config.getDouble("spawnpoints." + spawnNumber + ".pitch");

        return new Location(world, x, y, z, yaw, pitch);
    }
    public Location getPlayerSpawnLocation(UUID playerId) {
        Bukkit.getLogger().info("Map State at Retrieval: " + playerSpawnMap.toString());
        Integer spawnNumber = playerSpawnMap.get(playerId);
        if (spawnNumber != null) {
            Bukkit.getLogger().info("Found spawn number " + spawnNumber + " for " + playerId);
            return getSpawnLocation(spawnNumber);
        } else {
            Bukkit.getLogger().info("No spawn number found for " + playerId);
            return null;
        }
    }

}