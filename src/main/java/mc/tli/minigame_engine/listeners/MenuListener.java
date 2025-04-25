package mc.tli.minigame_engine.listeners;
//this class is used to handle the menu for the mini-game engine

import mc.tliUtils.Guis;
import mc.tliUtils.Utilities;

import mc.tli.minigame_engine.GameState;
import mc.tli.minigame_engine.TliMinigameEngine;
import mc.tli.minigame_engine.instance.Arena;
import mc.tli.minigame_engine.managers.ConfigManager;

import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MenuListener implements Listener {
    private final TliMinigameEngine plugin;
    private final ConfigManager configManager;

    public MenuListener(TliMinigameEngine plugin) {
        this.plugin = plugin;
        this.configManager = new ConfigManager(plugin);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();
        if(mainHandItem.getType() == Material.AIR){
            System.out.println("Item in main hand is not a compass");
            return;
        }
        //get the TestGame inventory from guis from the utils plugin
        Inventory selector = Guis.TestGame(player);
        if(player.getInventory().getItemInMainHand().getType().equals(Material.COMPASS)){
            System.out.println("Open menu");
            player.openInventory(selector);
        }
    }
    //this method is used to handle the click events in the menu
    @EventHandler
    public void onClick(InventoryClickEvent event){

        String menuTitle =  event.getView().title().toString();
        if(Utilities.removeColourCodes(menuTitle).equals("Test") && event.getCurrentItem() != null){
            Player player = (Player) event.getWhoClicked();
            switch(event.getRawSlot()){
                case 22:
                    List<Arena> Arenas =  plugin.getArenaManager().getArenas();
                    if(Arenas != null){
                        if(!Arenas.isEmpty()){
                            for(Arena arena : Arenas){
                                //check if the arena is queueing or countdown and if the max player count hase NOT been reached then add the player
                                if(arena.getState() == GameState.QUEUEING || arena.getState() == GameState.COUNTINGDOWN && arena.getPlayers().size() <= configManager.getMaxPlayers()){
                                    arena.addPlayer(player);
                                }else{
                                    player.sendMessage(NamedTextColor.RED + "An error hase occurred please try again");
                                    return;
                                }
                            }
                        }else{
                            player.sendMessage(NamedTextColor.RED + "No arenas found making arena try again");
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
