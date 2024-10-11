package catnoob.lightplugin;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import catnoob.lightplugin.Dogs.dog;
import catnoob.lightplugin.Foxs.Fox;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage("command can only use by player only");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0){
            player.sendMessage("§f[§6CatNoobLightPlugin§f] §6作者: §f貓咪村伺服器 : 貓貓");
            player.sendMessage("§7讓這束暖光，擁抱所有迷途的貓們。願世上再也沒有欺騙、再也沒有謊言與冰冷");
            player.sendMessage("§7> 如果可以的話，可以給我一句鼓勵或是抱抱。讓我更有動力:P <");
            return true;
        }
        if (args.length == 1){
            if (args[0].equals("reload")){
                if (!player.hasPermission("catnoobLight.reload")){
                    player.sendMessage("§f[§6CatNoobLightPlugin§f] 無權限使用此指令");
                }else {
                    LightPlugin.getPlugin().reloadConfig();
                    player.sendMessage("§f[§6CatNoobLightPlugin§f] 已重新載入config.yml(若需冷調整database.yml則需要關閉調整後重啟)");
                }
                return true;
            }else if (args[0].equals("list")){
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        printPlayerBlocks(player);
                    }
                }.runTaskAsynchronously(LightPlugin.getPlugin());
            }
        }
        return true;
    }
    Map<UUID, Set<Location>> PlayerBlockList = dog.get_map();
    public void printPlayerBlocks(Player player) {
        UUID uuid = player.getUniqueId();
        Set<Location> blockLocations = PlayerBlockList.get(uuid);
        if (blockLocations == null) {
            player.sendMessage(Fox.No_Block_placed());
            return;
        }
        player.sendMessage(Fox.List_UI());
        for (Location loc : blockLocations) {
            String worldName = getWorldName(loc.getWorld().getName());
            player.sendMessage(String.format("§f世界: §7%s §f| X: §7%d §f| Y: §7%d §f| Z: §7%d\n", worldName, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()
            ));
        }
    }

    private String getWorldName(String worldName) {
        switch (worldName.toLowerCase()) {
            case "world":
                return "主世界";
            case "world_nether":
                return "地獄";
            case "world_the_end":
                return "終界";
            default:
                return "未知世界";
        }
    }
}
