package catnoob.lightplugin.Dogs;
import catnoob.lightplugin.LightPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class dog {
    public static Map<Location, UUID> BlockToUidMap = new HashMap<>(); //L1 Point <> L2
    public static Map<UUID, Set<Location>> PlayerBlockList = new HashMap<>(); //L2 -> L1
    // 查看當前玩家放置數量
    public static int GetPlayerCurLimit (UUID uuid){
        if (PlayerBlockList.get(uuid) == null){
            return 0;
        }
        return PlayerBlockList.get(uuid).size();
    }
    // 刪除位置
    public static UUID RemoveLoc (Location location){
        if (BlockToUidMap.get(location) == null){
            return null;
        }
        UUID uuid = BlockToUidMap.remove(location);
        PlayerBlockList.get(uuid).remove(location);
        return uuid;
    }
    // 加入位置
    public static void AddLoc (Location location,UUID uuid){
        if (BlockToUidMap.get(location) == null){
            BlockToUidMap.put(location,uuid);
            Set<Location> new_set = new HashSet<>();
            new_set.add(location);
            PlayerBlockList.put(uuid,new_set);
            return;
        }
        BlockToUidMap.put(location,uuid);
        Set<Location> cat = PlayerBlockList.get(uuid);
        cat.add(location);
        PlayerBlockList.put(uuid,cat);
    }


    //
    public static boolean IsReachLimit(Player player,int cur){
        return !player.hasPermission("catnoobLight.limit." + cur+1);
        // if reach than return true;
    }
}