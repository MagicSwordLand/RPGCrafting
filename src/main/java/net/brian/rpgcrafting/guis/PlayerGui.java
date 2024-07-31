package net.brian.rpgcrafting.guis;

import net.brian.rpgcrafting.RPGCrafting;
import net.brian.rpgcrafting.guis.element.*;
import net.brian.rpgcrafting.guis.element.RetrieveElement;
import net.brian.rpgcrafting.player.PlayerCraftingProfile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerGui extends net.brian.rpgcrafting.guis.element.InventoryGui {

    public PlayerGui(PlayerCraftingProfile profile, Player player) {
        super(RPGCrafting.getPlugin(), null,player.getName()+"的合成列表",new String[]{
                "         ",
                " iiiiiii ",
                " iiiiiii ",
                " iiiiiii ",
                " iiiiiii ",
                "  p b n  "
        });
        setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        addElement(new GuiBackElement('b',new ItemStack(Material.ARROW),
                "&f返回"));
        GuiElementGroup group = generateGroup(profile);
        addElement(group);
        setCloseAction(close -> {
            Bukkit.getScheduler().runTaskLater(RPGCrafting.getPlugin(), this::destroy,5L);
            return false;
        });
    }

    GuiElementGroup generateGroup(PlayerCraftingProfile profile){
        GuiElementGroup group = new GuiElementGroup('i');
        profile.craftingItems.forEach(craftingItem -> {
            if(craftingItem.craftComplete()){
                group.addElement(new RetrieveElement('i',new ItemStack(Material.AIR),
                        click->{
                            profile.craftingItems.remove(craftingItem);
                            return true;
                        },craftingItem.getResult(),craftingItem.getName(),"","&e製作完成點擊領取"
                ));
            }
            else {
                group.addElement(new StaticGuiElement('i',craftingItem.getResult()
                        ,craftingItem.getName(),"","&f 剩餘時間: "+craftingItem.getRemainingTime()));
            }
        });
        return group;
    }

}
