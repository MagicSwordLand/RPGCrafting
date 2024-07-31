package net.brian.rpgcrafting;

import dev.reactant.reactant.core.ReactantPlugin;
import io.lumine.mythic.lib.api.item.NBTItem;
import net.Indyuce.mmoitems.api.item.mmoitem.LiveMMOItem;
import net.brian.playerdatasync.PlayerDataSync;
import net.brian.rpgcrafting.compatible.MMOItemsComp;
import net.brian.rpgcrafting.player.PlayerCraftingProfile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

@ReactantPlugin(servicePackages = "net.brian.rpgcrafting")
public class RPGCrafting extends JavaPlugin implements Listener {


    private static RPGCrafting plugin;

    @Override
    public void onLoad(){
        new MMOItemsComp(this);

    }

    @Override
    public void onEnable(){
        plugin = this;
        PlayerDataSync.getInstance().register("crafting", PlayerCraftingProfile.class);
        getServer().getPluginManager().registerEvents(this,this);
    }


    public static RPGCrafting getPlugin() {
        return plugin;
    }

}

