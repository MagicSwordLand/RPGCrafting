package net.brian.rpgcrafting.service;

import dev.reactant.reactant.core.component.Component;
import dev.reactant.reactant.core.component.lifecycle.LifeCycleHook;
import dev.reactant.reactant.core.dependency.injection.Inject;
import dev.reactant.reactant.extra.config.type.MultiConfigs;
import io.github.clayclaw.clawlibrary.reloader.ReloaderComponent;
import net.brian.rpgcrafting.configs.CraftingStation;
import net.brian.rpgcrafting.guis.CraftingGui;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

@Component
public class CraftingService implements LifeCycleHook {

    @Inject(name = "plugins/Crafting/recipes")
    MultiConfigs<CraftingStation> multiConfigs;

    HashMap<String,CraftingStation> stationMap = new HashMap<>();


    public void reload(){
        multiConfigs.getAll(true).blockingForEach(it->{
            it.refresh();
            String abPath = it.getPath();
            String path = abPath.substring(abPath.indexOf("recipes"+File.separator)+8,abPath.indexOf(".json"));
            CraftingStation station = it.getContent();
            station.setUp();
            stationMap.put(path,station);
        });
    }

    public CraftingStation getStation(String id){
        return stationMap.get(id);
    }

    public void openStation(String id,Player player){
        CraftingStation station = getStation(id);
        if(station != null){
            new CraftingGui(station,player).show(player);
        }
    }

    public Set<String> getStationsID(){
        return stationMap.keySet();
    }

    public Collection<CraftingStation> getStations(){
        return stationMap.values();
    }

}
