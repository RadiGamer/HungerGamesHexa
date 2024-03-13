package org.hexa.hungergameshexa.manager;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.hexa.hungergameshexa.HungerGamesHexa;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ChestManagerTEST {
    private Plugin plugin;
    private FileConfiguration inventariosConfig;
    private File inventariosFile;
    private Map<String, Inventory> inventarios;

    public ChestManagerTEST(HungerGamesHexa plugin){
        this.plugin = plugin;
        inventarios = new HashMap<>();
        inventariosFile = new File(plugin.getDataFolder(),"inventarios.yml");
        inventariosConfig = YamlConfiguration.loadConfiguration(inventariosFile);
        inicializarInventarios();
        if (!inventariosFile.exists()) {
            try {
                inventariosFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        inicializarInventarios();
    }
    public void inicializarInventarios(){
        for(String tipo: inventariosConfig.getKeys(false)){
            inventarios.put(tipo, cargarInventario(tipo));
            System.out.println("Inventarios init.");
        }
    }
    public void guardarInventario(String tipo, Inventory inventario) {
        inventarios.put(tipo, inventario);

        String path = "inventarios." + tipo;
        inventariosConfig.set(path, null);

        for (int i = 0; i < inventario.getSize(); i++) {
            ItemStack item = inventario.getItem(i);
            if (item != null) {
                inventariosConfig.set(path + "." + i, item);
            }
        }try {
            inventariosConfig.save(inventariosFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Inventory obtenerInventario(String tipo) {
        System.out.println("Obteniendo inventario: " + tipo);
        return inventarios.getOrDefault(tipo, null);
    }

    private Inventory cargarInventario(String tipo) {
        Inventory inventario = Bukkit.createInventory(null, 27, tipo);
        System.out.println("Intentando cargar inventario: " + tipo);

        String path = "inventarios." + tipo;
        ConfigurationSection section = inventariosConfig.getConfigurationSection(path);

        if (section != null) {
            for (String key : section.getKeys(false)) {
                int slot = Integer.parseInt(key);
                ItemStack item = inventariosConfig.getItemStack(path + "." + key);
                inventario.setItem(slot, item);
            }
        } else {
            System.out.println("La sección de configuración para " + tipo + " no existe.");
        }
        System.out.println("Inventario cargado correctamente:" + tipo);
        return inventario;
    }
}

