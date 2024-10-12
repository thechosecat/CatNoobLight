package catnoob.lightplugin.Dogs;

import catnoob.lightplugin.LightPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class dog {

    public static Map<Location, UUID> BlockToUidMap = new HashMap<>(); //L1 Point <> L2
    public static Map<UUID, Set<Location>> PlayerBlockList = new HashMap<>(); //L2 -> L1

    // check player limit (cur)
    public static int GetPlayerCurLimit(UUID uuid) {
        Set<Location> playerBlocks = PlayerBlockList.get(uuid);
        return (playerBlocks == null) ? 0 : playerBlocks.size();
    }

    // delete loc from map
    public static UUID RemoveLoc(Location location) {
        UUID uuid = BlockToUidMap.remove(location);

        if (uuid != null) {
            Set<Location> playerBlocks = PlayerBlockList.get(uuid);
            playerBlocks.remove(location);

            if (playerBlocks.isEmpty()) {
                PlayerBlockList.remove(uuid);
                LightPlugin.getFox().remove_uuid(uuid); // Cleanup when no blocks remain
            }
        }

        return uuid;
    }

    // add loc to map
    public static void AddLoc(Location location, UUID uuid) {
        BlockToUidMap.put(location, uuid);

        PlayerBlockList.computeIfAbsent(uuid, k -> new HashSet<>()).add(location);
    }

    public static Map<UUID, Set<Location>> get_map() {
        return PlayerBlockList;
    }

    // check if the player has reached their block limit
    public static boolean IsReachLimit(Player player, int cur) {
        return player.hasPermission("catnoobLight.limit." + cur);
    }
}