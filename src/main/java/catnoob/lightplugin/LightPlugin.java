package catnoob.lightplugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class LightPlugin extends JavaPlugin {
    public static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
        // Plugin startup logic
        FileConfiguration config = this.getConfig();
        config.options().copyDefaults(true);
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Plugin getPlugin(){
        return plugin;
    }
}
