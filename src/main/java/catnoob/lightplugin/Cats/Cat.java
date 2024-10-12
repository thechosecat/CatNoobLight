package catnoob.lightplugin.Cats;

import catnoob.lightplugin.Dogs.dog;
import catnoob.lightplugin.Foxs.Fox;
import catnoob.lightplugin.LightPlugin;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class Cat implements Listener {
    private static DataStore dataStore = null;
    public Cat(){
        if (LightPlugin.UseGriefPrevention()){
            dataStore = GriefPrevention.instance.dataStore;
        }
    }
    public void mark (Player player,Block block){
        player.spawnParticle(
                Particle.BLOCK_MARKER,
                block.getLocation().add(0.5, 0.5, 0.5),
                1, block.getBlockData()
        );
    }
    public void UnMark (Player player,Block block){
        player.spawnParticle(
                Particle.SMOKE_NORMAL,
                block.getLocation().add(0.5, 0.5, 0.5),
                0,null
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
            int range = Fox.range();
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
        if (player.getGameMode() != GameMode.SURVIVAL){
            return;
        }
        if (!player.hasPermission("catnoobLight.use")){
            event.setCancelled(true);
            player.sendMessage(Fox.no_uses());
            return;
        }
        if (player.hasPermission("catnoobLight.limit.-1")){
            return;
        }
        UUID uuid = player.getUniqueId();
        if (dog.IsReachLimit(player,dog.GetPlayerCurLimit(uuid))){
            //over limit
            player.sendMessage(Fox.reach_limit(dog.GetPlayerCurLimit(uuid)));
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
            if (LightPlugin.UseGriefPrevention()){
                Claim claim = dataStore.getClaimAt(player.getLocation(),true,null);
                if (claim != null){
                    if (!claim.hasExplicitPermission(player, ClaimPermission.Access)){
                        player.sendMessage(Fox.No_claim_adjust_per(claim));
                        return;
                    }
                }
            }
            if (player.isSneaking()){
                return;
            }
            // adjust light
            Levelled data = (Levelled) block.getBlockData().clone();
            data.setLevel((data.getLevel() + 1) % 16);
            block.setBlockData(data);
            mark(player, block);
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
        UnMark(player,block);
        UUID uuid = dog.RemoveLoc(block.getLocation());
        if (uuid == null){
            return;
        }
        if (uuid.equals(player.getUniqueId())){
            return;
        }
        player.sendMessage(Fox.remove_others_light(uuid));
    }
}
