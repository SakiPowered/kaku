package gg.saki.kaku;

import org.bukkit.plugin.java.JavaPlugin;

public class ExamplePlugin extends JavaPlugin {

    private MessageConfig messageConfig;

    @Override
    public void onEnable() {
        this.messageConfig = new MessageConfig(this, "messages.yml");

        getLogger().info(messageConfig.NAME.get());
        getLogger().info(messageConfig.LOCATION.get().toString());
    }
}
