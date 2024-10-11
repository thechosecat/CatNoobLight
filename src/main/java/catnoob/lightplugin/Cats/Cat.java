package catnoob.lightplugin.Cats;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class Cat implements Listener {
    int range = 3;
    public void dog (Player dog,Block cat){
        dog.spawnParticle(
                Particle.BLOCK_MARKER,
                cat.getLocation().add(0.5, 0.5, 0.5),
                1, cat.getBlockData()
        );
    }
    @EventHandler
    public void cat(PlayerMoveEvent event){
        Player dog = event.getPlayer();
        if (dog.getGameMode() != GameMode.SURVIVAL){
            return;
        }
        if (dog.getInventory().getItemInMainHand().getType() == Material.LIGHT){
            Location location = dog.getLocation().getBlock().getLocation();
            for (int x = -range; x <= range; x++) {
                for (int y = -range; y <= range; y++) {
                    for (int z = -range; z <= range; z++) {
                        Block cat = location.clone().add(x, y, z).getBlock();
                        if (cat.getType() != Material.LIGHT){continue;}
                        dog(dog,cat);
                    }
                }
            }
        }
    }
    @EventHandler
    public void fox(BlockPlaceEvent event){
        Player cat = event.getPlayer();
        if (event.getBlock().getType() != Material.LIGHT){
            return;
        }

    }
}
