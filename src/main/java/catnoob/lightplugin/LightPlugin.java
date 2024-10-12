package catnoob.lightplugin;

import catnoob.lightplugin.Cats.Cat;
import catnoob.lightplugin.Foxs.Fox;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class LightPlugin extends JavaPlugin {

//   ==============================
//   = Cat    = dog   = Fox       =
//   ==============================
//   = Events = Cache = config/DB =
//   ==============================
//
//   Framework:DB           <> L1           <> L2
//   Name     :Database.yml    BlockToUidMap   PlayerBlockList
//   Hardware :(Disk)          (ram)           (ram)
//   Use in   :En/disable      Plugin events   Plugin events
//
    public static Plugin plugin;
    public static boolean UseGriefPrevention = false;
    public static Fox fox;
    @Override
    public void onEnable() {
        plugin = this;
        Bukkit.getServer().getConsoleSender().sendMessage("[CatNoobLightPlugin] " + ChatColor.RED+"Loading...");
        // =======hooks=========
        if (getServer().getPluginManager().isPluginEnabled("GriefPrevention")){
            Bukkit.getServer().getConsoleSender().sendMessage("[CatNoobLightPlugin] " + ChatColor.YELLOW+"Hooked into" + ChatColor.GREEN+"GriefPrevention");
            UseGriefPrevention = true;
        }
        // config/DB
        FileConfiguration config = this.getConfig();
        config.options().copyDefaults(true);
        saveDefaultConfig();
        fox = new Fox();
        Bukkit.getServer().getConsoleSender().sendMessage("[CatNoobLightPlugin] " + ChatColor.YELLOW+"Loading DataBase.yml <> to map_Cache L1/L2");
        fox.loadall();
        Bukkit.getServer().getConsoleSender().sendMessage("[CatNoobLightPlugin] " + ChatColor.GREEN+"Cache loaded!");
        // register
        getServer().getPluginManager().registerEvents(new Cat(), this);
        getCommand("light").setExecutor(new Commands());
    }
    @Override
    public void onDisable() {
        fox.saveall();
    }
    public static Plugin getPlugin(){
        return plugin;
    }
    public static Fox getFox(){
        return fox;
    }
    public static Boolean UseGriefPrevention(){
        return UseGriefPrevention;
    }
}
