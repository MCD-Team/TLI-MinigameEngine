package mc.tli.minigame_engine.listeners;

import mc.tLIUtils.Utils;
import mc.tLIUtils.guis;
import mc.tLIUtils.utilities;
import mc.tli.minigame_engine.GameState;
import mc.tli.minigame_engine.TliMinigameEngine;
import mc.tli.minigame_engine.instance.Arena;
import mc.tli.minigame_engine.managers.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MenuListener implements Listener {
    private final TliMinigameEngine plugin;
    public MenuListener(TliMinigameEngine plugin) {
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();
        if(mainHandItem.getType() == Material.AIR){
            System.out.println("Item in main hand is not a compass");
            return;
        }

        player.sendMessage("Open menu");
        Inventory selector = guis.TestGame(player);
        if(player.getInventory().getItemInMainHand().getType().equals(Material.COMPASS)){
            System.out.println("Open menu");
            player.openInventory(selector);

        }
    }
    @EventHandler
    public void onClick(InventoryClickEvent event){
        String menuTitle = event.getView().getTitle();
        if(utilities.removeColourCodes(menuTitle).equals("Test") && event.getCurrentItem() != null){
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            switch(event.getRawSlot()){
                case 22:
                    List<Arena> Arenas =  plugin.getArenaManager().getArenas();
                    if(Arenas != null){
                        if(!Arenas.isEmpty()){
                            for(Arena arena : Arenas){
                                //check if the arena is queueing or countdown and if the max player count hase NOT been reached then add the player
                                if(arena.getState() == GameState.QUEUEING || arena.getState() == GameState.COUNTINGDOWN && arena.getPlayers().size() <= ConfigManager.getMaxPlayers()){
                                    arena.addPlayer(player);
                                }else{
                                    player.sendMessage(ChatColor.RED + "An error hase occurred please try again");
                                    return;
                                }
                            }
                        }else{
                            player.sendMessage(ChatColor.RED + "No arenas found making arena try again");
                        }
                    }
                    break;
                default:
                    player.closeInventory();
                    break;
            }
            player.closeInventory();
        }
    }
}
