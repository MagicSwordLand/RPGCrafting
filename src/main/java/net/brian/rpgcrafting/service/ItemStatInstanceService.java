package net.brian.rpgcrafting.service;

import dev.reactant.reactant.core.component.Component;
import dev.reactant.reactant.core.component.lifecycle.LifeCycleHook;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.stat.CustomModelData;
import net.Indyuce.mmoitems.stat.GemSockets;
import net.Indyuce.mmoitems.stat.Soulbound;
import net.Indyuce.mmoitems.stat.type.ItemStat;
import net.brian.soulbelonggui.SoulBelongGUI;
import net.brian.soulbelonggui.stat.SoulBelongStat;

@Component
public class ItemStatInstanceService implements LifeCycleHook {


    public GemSockets GEM_SOCKETS;
    public Soulbound SOULBOUND;
    public CustomModelData CUSTOM_MODEL;
    public ItemStat SOUL_BELONG;

    @Override
    public void onEnable(){
        GEM_SOCKETS = (GemSockets) MMOItems.plugin.getStats().get("GEM_SOCKETS");
        SOULBOUND = (Soulbound) MMOItems.plugin.getStats().get("SOULBOUND");
        SOUL_BELONG = SOULBOUND;
        //SOUL_BELONG = MMOItems.plugin.getStats().get("SOULBELONG");
        CUSTOM_MODEL = (CustomModelData) MMOItems.plugin.getStats().get("CUSTOM_MODEL_DATA");
    }

}
