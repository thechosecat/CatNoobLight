package catnoob.lightplugin.Cats;

import catnoob.lightplugin.Dogs.dog;
import catnoob.lightplugin.LightPlugin;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class Cat implements Listener {
    private Plugin main = LightPlugin.plugin;
    int range = 3;
    public void mark (Player player,Block block){
        player.spawnParticle(
                Particle.BLOCK_MARKER,
                block.getLocation().add(0.5, 0.5, 0.5),
                1, block.getBlockData()
        );
    }
    @EventHandler
    public void cat(PlayerMoveEvent event){
        Player player = event.getPlayer();
        if (player.getGameMode() != GameMode.SURVIVAL){
            return;
        }
        if (player.getInventory().getItemInMainHand().getType() == Material.LIGHT){
            Location location = player.getLocation().getBlock().getLocation();
            for (int x = -range; x <= range; x++) {
                for (int y = -range; y <= range; y++) {
                    for (int z = -range; z <= range; z++) {
                        Block block = location.clone().add(x, y, z).getBlock();
                        if (block.getType() != Material.LIGHT){continue;}
                        mark(player,block);
                    }
                }
            }
        }
    }
    @EventHandler
    public void fox(BlockPlaceEvent event){
        Player player = event.getPlayer();
        if (event.getBlock().getType() != Material.LIGHT){
            return;
        }
        if (!player.hasPermission("catnoobLight.use")){
            event.setCancelled(true);
            player.sendMessage(main.getConfig().getString("no_uses"));
            return;
        }
        if (player.hasPermission("catnoobLight.limit.-1")){
            return;
        }
        UUID uuid = player.getUniqueId();
        if (dog.IsReachLimit(player,dog.GetPlayerCurLimit(uuid))){
            //超過限制
            player.sendMessage(
                    main.getConfig().getString("reach_limit")
                            .replace('&', '$')
                            .replace("%mount%", String.valueOf(dog.GetPlayerCurLimit(uuid)))
            );
            event.setCancelled(true);
            return;
        }
        // add locate to ram_map <> disk
        dog.AddLoc(event.getBlock().getLocation(),uuid);
    }
    @EventHandler
    public void dog(PlayerInteractEvent event){
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        if (block == null){
            return;
        }
        if (block.getType() != Material.LIGHT){
            return;
        }
        if (player.getGameMode()!=GameMode.SURVIVAL){
            return;
        }
        if (player.getInventory().getItemInMainHand().getType() != Material.LIGHT){
            return;
        }
        Action action = event.getAction();
        if (action.isLeftClick()){
            // break light
            if (player.breakBlock(block)){
                Location location = block.getLocation();
                dog.RemoveLoc(location);
                block.getWorld().dropItem(location, new ItemStack(Material.LIGHT));
            }
            return;
        }
        if (action.isRightClick()){
            // adjust light
            Levelled data = (Levelled) block.getBlockData();
            data.setLevel((data.getLevel() + 1) % 16);
            block.setBlockData(data);
            mark(player,block);
        }
    }
    @EventHandler(priority = EventPriority.LOW)
    public void wolf(BlockBreakEvent event){
        Block block = event.getBlock();
        Player player = event.getPlayer();
        if (block.getType() != Material.LIGHT){
            return;
        }
        if (event.isCancelled()){
            return;
        }
        UUID uuid = dog.RemoveLoc(block.getLocation());
        if (uuid == null){
            return;
        }
        if (uuid == player.getUniqueId()){
            return;
        }
        player.sendMessage(
                main.getConfig().getString("remove_others_light")
                        .replace('&', '$')
                        .replace("%player%", Bukkit.getOfflinePlayer(uuid).getName())
        );
    }
}
