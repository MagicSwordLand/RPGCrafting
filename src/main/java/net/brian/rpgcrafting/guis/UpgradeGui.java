package net.brian.rpgcrafting.guis;

import io.lumine.mythic.lib.api.item.NBTItem;
import net.Indyuce.mmoitems.api.item.build.ItemStackBuilder;
import net.Indyuce.mmoitems.api.item.mmoitem.LiveMMOItem;
import net.Indyuce.mmoitems.stat.GemSockets;
import net.brian.rpgcrafting.RPGCrafting;
import net.brian.rpgcrafting.compatible.MMOItemsComp;
import net.brian.rpgcrafting.configs.UpgradeConfig;
import net.brian.rpgcrafting.guis.element.*;
import net.brian.rpgcrafting.service.UpgradeService;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;

public class UpgradeGui extends net.brian.rpgcrafting.guis.element.InventoryGui {

    UpgradeService upgradeService;

    GuiStorageElement a,b,c,d;
    StaticGuiElement f,g;
    RetrieveElement e;
    Inventory inv;

    float successChance = 0,downGradeChance = 0;

    boolean consumeA = false,consumeB = false,canUpgrade = false;

    Player player;

    public UpgradeGui(UpgradeService upgradeService,Player player) {
        super(RPGCrafting.getPlugin(), "",new String[]{
                "         ",
                "   a a   ",
                "  a a e  ",
                "    f    ",
                "    g    ",
                "         "
        });
        this.upgradeService = upgradeService;
        this.player = player;
        /*
          a = 提升機率
          b = 降低下降機率
          c = 強化物品
          d = 強化石
          e = 結果
          f = 顯示成功,下降機率
         */
        inv = Bukkit.createInventory(null,54);
        a = new GuiStorageElement('a',inv,-1,this::calculateChance,null);
        e = new RetrieveElement('e',new ItemStack(Material.AIR),click->true,new ItemStack(Material.AIR));
        f = new StaticGuiElement('f',new ItemStack(Material.EMERALD),
                "&e強化詳情","&e成功機率: 0%","&e降級機率: 0%");
        g = new StaticGuiElement('g',new ItemStack(Material.ANVIL),click -> {
            upgrade((Player) click.getWhoClicked());
            return true;
        }, "強化");

        addElement(a);
        addElement(e);
        addElement(f);
        addElement(g);
        setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));

        setCloseAction(close -> {
            Arrays.stream(inv.getContents()).filter(Objects::nonNull).forEach(item->{
                if(!player.getInventory().addItem(item).isEmpty()){
                    player.getWorld().dropItem(player.getLocation(),item,entity->{
                        entity.setOwner(player.getUniqueId());
                        player.sendMessage("你的包包滿了，素材已掉到地上");
                    });
                }
            });
            Bukkit.getScheduler().runTaskLater(RPGCrafting.getPlugin(), this::destroy,5L);
            return false;
        });
    }

    private void calculateChance(){
        String type,id;
        NBTItem c = NBTItem.get(inv.getItem(2));
        if(c.hasType()){
            type = c.getString("MMOITEMS_ITEM_TYPE");id = c.getString("MMOITEMS_ITEM_ID");
            UpgradeConfig template = upgradeService.getTemplate(type,id);
            LiveMMOItem liveMMOItem = new LiveMMOItem(c);
            int currentLevel = liveMMOItem.getUpgradeLevel();
            if(template == null){
                reset();
                return;
            }
            UpgradeConfig.UpgradeInfo info = template.map.get(currentLevel);
            if(info != null && liveMMOItem.hasUpgradeTemplate() && liveMMOItem.getMaxUpgradeLevel() > currentLevel){
                canUpgrade = true;
            }else {
                reset();
                return;
            }

            successChance = info.successChance;
            downGradeChance = info.downGradeChance;

            NBTItem d = NBTItem.get(inv.getItem(3));
            if(d.hasTag(MMOItemsComp.SUCCESSRATE)){
                successChance += 0.01*d.getDouble(MMOItemsComp.SUCCESSRATE);
                canUpgrade = true;
            }else {
                reset();
                return;
            }
            if(d.hasTag(MMOItemsComp.AVOIDDOWNGRADE)){
                downGradeChance -= 0.01*d.getDouble(MMOItemsComp.AVOIDDOWNGRADE);
            }

            NBTItem a = NBTItem.get(inv.getItem(0));
            if(a.hasTag(MMOItemsComp.ADDITIONAL_SUCCESS)){
                successChance += 0.01*a.getDouble(MMOItemsComp.ADDITIONAL_SUCCESS);
                consumeA = true;
            }

            NBTItem b = NBTItem.get(inv.getItem(1));
            if(b.hasTag(MMOItemsComp.AVOIDDOWNGRADE)){
                downGradeChance -= 0.01*b.getDouble(MMOItemsComp.AVOIDDOWNGRADE);
                consumeB = true;
            }

            f.setText("&e強化詳情","&e成功機率: "+(int) (successChance*100)+"%","&e降級機率: "+(int)(downGradeChance*100)+"%");
            Bukkit.getScheduler().runTaskLater(RPGCrafting.getPlugin(), (Runnable) this::draw,1);
            return;
        }
        reset();
        setCloseAction(close -> {
            Arrays.stream(inv.getContents()).filter(Objects::nonNull).forEach(item->{
                if(!player.getInventory().addItem(item).isEmpty()){
                    player.getWorld().dropItem(player.getLocation(),item,entity->{
                        entity.setOwner(player.getUniqueId());
                        player.sendMessage("你的包包滿了，合成素材已掉到地上");
                    });
                }
            });
            return false;
        });
    }

    private void reset(){
        successChance = 0;
        consumeB = false;
        consumeA = false;
        downGradeChance = 0;
        canUpgrade = false;
        f.setText("&e強化詳情","&e成功機率: 0%","&e降級機率: 0%");
        Bukkit.getScheduler().runTaskLater(RPGCrafting.getPlugin(), (Runnable) this::draw,1);
    }

    private void upgrade(Player player){
        if(!canUpgrade) return;
        if(e.getReward() != null && !e.getReward().getType().equals(Material.AIR)){
            return;
        }
        ItemStack c = inv.getItem(2);
        NBTItem nbtItem = NBTItem.get(c);
        LiveMMOItem mmoItem = new LiveMMOItem(nbtItem);
        if(Math.random()<=successChance){
            mmoItem.upgrade();
            ItemStackBuilder builder = mmoItem.newBuilder();
            e.setReward(builder.build());
            ItemStack upgradeItem = inv.getItem(2);
            upgradeItem.setAmount(upgradeItem.getAmount()-1);
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE,1,1);
        }
        else {
            player.playSound(player.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,1,1);
            if(Math.random()<=downGradeChance){
                player.sendMessage("失敗 降級為"+(mmoItem.getUpgradeLevel()-1) +"haha");
                inv.setItem(2,upgradeService.downGrade(mmoItem));
            }
            else {
                player.sendMessage("失敗 不降級");
            }
        }
        if(consumeA){
            ItemStack itemStack = inv.getItem(0);
            if(itemStack != null){
                itemStack.setAmount(itemStack.getAmount()-1);
            }
        }
        if(consumeB){
            ItemStack itemStack = inv.getItem(1);
            if(itemStack != null){
                itemStack.setAmount(itemStack.getAmount()-1);
            }
        }
        ItemStack upgradeStone = inv.getItem(3);
        upgradeStone.setAmount(upgradeStone.getAmount()-1);
        reset();
        calculateChance();
    }




}
