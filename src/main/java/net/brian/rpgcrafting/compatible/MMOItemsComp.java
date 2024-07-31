package net.brian.rpgcrafting.compatible;

import dev.reactant.reactant.extra.command.PermissionRoot;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.stat.type.DoubleStat;
import net.brian.rpgcrafting.RPGCrafting;
import org.bukkit.Material;

public class MMOItemsComp {

    public static DoubleStat avoidDownGrade,successRate,additionSuccess;
    public static DoubleStat boostCraftTime;
    private final RPGCrafting plugin;

    public static final String AVOIDDOWNGRADE = "MMOITEMS_AVOIDDOWNGRADE";
    public static final String SUCCESSRATE = "MMOITEMS_SUCCESSRATE";
    public static final String ADDITIONAL_SUCCESS = "MMOITEMS_ADDITIONAL_SUCCESS";

    public static final String BOOST_CRAFT_TIME = "MMOITEMS_BOOST_CRAFT_TIME";

    public MMOItemsComp(RPGCrafting plugin){
        this.plugin = plugin;
        avoidDownGrade = new DoubleStat("AVOIDDOWNGRADE", Material.DIAMOND,
                "避免下降",new String[]{"避免強化失敗等級下降"},new String[]{"all"});
        successRate = new DoubleStat("SUCCESSRATE",Material.GOLD_INGOT,
                "強化石提升機率",new String[]{"用來設定強化石的提升機率"},new String[]{"all"});
        additionSuccess = new DoubleStat("ADDITIONAL_SUCCESS",Material.IRON_INGOT,
                "除了強化石以外提升成功的機率",new String[]{"用來提升額外的成功機率"},new String[]{"all"});
        boostCraftTime = new DoubleStat("BOOST_CRAFT_TIME", Material.CLOCK,
                "減少合成時間",new String[]{"減少合成時間"});
        MMOItems.plugin.getStats().register(additionSuccess);
        MMOItems.plugin.getStats().register(successRate);
        MMOItems.plugin.getStats().register(avoidDownGrade);
        MMOItems.plugin.getStats().register(boostCraftTime);
    }


}
