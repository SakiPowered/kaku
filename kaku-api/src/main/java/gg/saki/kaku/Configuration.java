package gg.saki.kaku;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class Configuration extends YamlConfiguration {

    private final JavaPlugin plugin;
    private final File file;

    private final Map<String, Leaf<?>> defaults;

    public Configuration(JavaPlugin plugin, File file) {
        this.plugin = plugin;
        this.file = file;

        this.defaults = new HashMap<>();

        if (!file.exists()) {
            // create file from plugin resources folder
            plugin.saveResource(file.getName().endsWith(".yml") ? file.getName() : file.getName() + ".yml", false);
        }

        this.load();
    }

    public Configuration(JavaPlugin plugin, String fileName) {
        this(plugin, new File(plugin.getDataFolder(), fileName));
    }

    public void load() {
        try {
            this.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            this.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T> Leaf<T> useDefault(Class<T> type, T value, String path){
        if(this.defaults.containsKey(path)){
            return (Leaf<T>) this.defaults.get(path);
        }

        Leaf<T> leaf = new Leaf<>(this, type, value, path);
        this.defaults.put(path, leaf);
        this.options().copyDefaults(true);
        this.save();
        return leaf;
    }
}
