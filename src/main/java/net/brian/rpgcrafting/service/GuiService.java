package net.brian.rpgcrafting.service;

import dev.reactant.reactant.core.component.Component;
import dev.reactant.reactant.core.component.lifecycle.LifeCycleHook;
import dev.reactant.reactant.core.dependency.injection.Inject;
import io.github.clayclaw.clawlibrary.reloader.ReloaderComponent;
import net.brian.playerdatasync.events.PlayerDataFetchComplete;
import net.brian.rpgcrafting.configs.CraftingRecipe;
import net.brian.rpgcrafting.guis.PlayerGui;
import net.brian.rpgcrafting.guis.RecipeGui;
import net.brian.rpgcrafting.guis.StationGui;
import net.brian.rpgcrafting.guis.element.InventoryGui;
import net.brian.rpgcrafting.player.PlayerCraftingProfile;
import net.brian.rpgcrafting.service.CraftingService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

@Component
public class GuiService implements LifeCycleHook, ReloaderComponent, Listener {

    @Inject
    CraftingService craftingService;

    HashMap<CraftingRecipe, RecipeGui> recipeGuis = new HashMap<>();
    HashMap<String, StationGui> stationGuiMap = new HashMap<>();


    @Override
    public void onEnable(){
        reload();
    }

    public void reload(){
        craftingService.reload();
        recipeGuis.values().forEach(InventoryGui::destroy);
        stationGuiMap.values().forEach(InventoryGui::destroy);
        recipeGuis.clear();
        stationGuiMap.clear();
        craftingService.getStationsID().forEach(key->{
            stationGuiMap.put(key,new StationGui(this,craftingService.getStation(key)));
        });
        craftingService.getStations().forEach(station->{
            station.getRecipes().forEach(recipe -> {
                recipeGuis.put(recipe,new RecipeGui(recipe));
            });
        });
    }


    public RecipeGui getRecipeGui(CraftingRecipe recipe){
        return recipeGuis.get(recipe);
    }

    public StationGui getStationGui(String id){
        return stationGuiMap.get(id);
    }

}
