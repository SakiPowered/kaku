package gg.saki.kaku;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class MessageConfig extends Configuration{

    public MessageConfig(JavaPlugin plugin, String fileName) {
        super(plugin, fileName);
    }

    public final Leaf<String> NAME = useDefault(String.class, "DylanDeNewb", "name");
    public final Leaf<Location> LOCATION = useDefault(Location.class, new Location(Bukkit.getWorld("world"), 0,0,0), "location");
}
