package catnoob.lightplugin.Foxs;

import catnoob.lightplugin.LightPlugin;
import me.ryanhamshire.GriefPrevention.Claim;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

import catnoob.lightplugin.Dogs.dog;


public class Fox {
    // ====================================== yml <> Cache ======================================
    private final File db = new File("plugins/CatNoobLightPlugin/database.yml");
    private final YamlConfiguration database;
    public Fox(){
        new File("plugins/CatNoobLightPlugin").mkdirs();
        database = YamlConfiguration.loadConfiguration(db);
        loadall();
    }
    public void loadall() {
        for (String uuidString : database.getKeys(false)) {
            UUID uuid = UUID.fromString(uuidString);
            List<String> locationStrings = database.getStringList(uuidString);
            for (String locString : locationStrings) {
                String[] parts = locString.split(",");
                double x = Double.parseDouble(parts[0]);
                double y = Double.parseDouble(parts[1]);
                double z = Double.parseDouble(parts[2]);
                String worldName = parts[3];
                World world = Bukkit.getWorld(worldName);
                dog.AddLoc(new Location(world, x, y, z),uuid);
            }
        }
    }
    public void saveall() {
        Map<UUID, Set<Location>> PlayerBlockList = dog.get_map();
        for (UUID uuid : PlayerBlockList.keySet()) {
            Set<Location> locations = PlayerBlockList.get(uuid);
            List<String> locationStrings = new ArrayList<>();
            for (Location loc : locations) {
                String locString = loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getWorld().getName();
                locationStrings.add(locString);
            }
            database.set(uuid.toString(), locationStrings);
        }
        try {database.save(db); } catch (IOException e) {e.printStackTrace(); }
    }
    // ==========================config.yml=======================
    private static final Plugin plugin = LightPlugin.getPlugin();

    public static String List_UI(){
        return plugin.getConfig().getString("List_UI").replace('&', '§');
    }
    public static String no_uses(){
        return plugin.getConfig().getString("no_uses").replace('&', '§');
    }
    public static String No_Block_placed(){
        return plugin.getConfig().getString("No_Block_placed").replace('&', '§');
    }
    public static String remove_others_light(UUID uuid){
        return plugin.getConfig().getString("remove_others_light")
                .replace('&', '§')
                .replace("%player%",Bukkit.getOfflinePlayer(uuid).getName());
    }
    public static String reach_limit(int limit){
                return plugin.getConfig().getString("reach_limit")
                        .replace('&', '§')
                        .replace("%mount%", String.valueOf(limit));
    }
    public static String No_claim_adjust_per(Claim claim){
        return plugin.getConfig().getString("No_claim_adjust_per")
                .replace('&', '§')
                .replace("%player%", String.valueOf(claim.getOwnerName()));
    }
    public static int range(){
        return plugin.getConfig().getInt("display_range");
    }

}
