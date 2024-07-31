package net.brian.rpgcrafting.service;

import dev.reactant.reactant.core.component.Component;
import dev.reactant.reactant.core.component.lifecycle.LifeCycleHook;
import dev.reactant.reactant.core.dependency.injection.Inject;
import dev.reactant.reactant.extra.config.type.MultiConfigs;
import io.github.clayclaw.clawlibrary.reloader.ReloaderComponent;
import io.lumine.mythic.lib.api.item.NBTItem;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.interaction.GemStone;
import net.Indyuce.mmoitems.api.item.build.ItemStackBuilder;
import net.Indyuce.mmoitems.api.item.mmoitem.LiveMMOItem;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.brian.rpgcrafting.configs.UpgradeConfig;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.logging.Level;

@Component
public class UpgradeService implements LifeCycleHook, ReloaderComponent {

    @Inject(name = "plugins/Crafting/upgrades")
    MultiConfigs<UpgradeConfig> multiConfigs;

    @Inject
    ItemStatInstanceService statInstanceService;

    HashMap<String,HashMap<String, UpgradeConfig>> templates = new HashMap<>();

    @Override
    public void onEnable(){
        reload();
    }


    @Override
    public void reload() {
        multiConfigs.getAll(true).blockingForEach(it->{
            UpgradeConfig template = it.getContent();
            HashMap<String, UpgradeConfig> map= templates.getOrDefault(template.type,new HashMap<>());
            map.put(template.id,template);
            templates.put(template.type,map);
        });
    }

    public UpgradeConfig getTemplate(String type, String id){
        if(templates.containsKey(type)){
            return templates.get(type).get(id);
        }
        return null;
    }


    public ItemStack downGrade(LiveMMOItem itemStack){
        LiveMMOItem target = new LiveMMOItem(MMOItems.plugin.getMMOItem(itemStack.getType(),itemStack.getId()).newBuilder().build());
        boolean hasGemStone = itemStack.hasData(statInstanceService.GEM_SOCKETS);

        int targetLevel = Math.max(0, itemStack.getUpgradeLevel()-1);
        if(target.hasUpgradeTemplate()){
            for(int i=0;i<targetLevel;i++){
                target.upgrade();
            }
        }

        if(hasGemStone){
            target.setStatHistory(statInstanceService.GEM_SOCKETS, itemStack.getStatHistory(statInstanceService.GEM_SOCKETS));
            target.setData(statInstanceService.GEM_SOCKETS, itemStack.getData(statInstanceService.GEM_SOCKETS));
        }

        if(itemStack.hasData(statInstanceService.SOULBOUND)){
            target.setData(statInstanceService.SOULBOUND, itemStack.getData(statInstanceService.SOULBOUND));
        }
        if(itemStack.hasData(statInstanceService.SOUL_BELONG)){
            target.setData(statInstanceService.SOUL_BELONG, itemStack.getData(statInstanceService.SOUL_BELONG));
        }
        if(itemStack.hasData(statInstanceService.CUSTOM_MODEL)){
            target.setData(statInstanceService.CUSTOM_MODEL, itemStack.getData(statInstanceService.CUSTOM_MODEL));
        }



        return target.newBuilder().build();

    }

}
