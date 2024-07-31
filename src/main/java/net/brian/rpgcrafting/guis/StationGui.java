package net.brian.rpgcrafting.guis;

import net.brian.rpgcrafting.RPGCrafting;
import net.brian.rpgcrafting.configs.CraftingStation;
import net.brian.rpgcrafting.guis.element.*;
import net.brian.rpgcrafting.player.PlayerCraftingProfile;
import net.brian.rpgcrafting.service.GuiService;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;


public class StationGui extends InventoryGui {

    public StationGui(GuiService guiService, CraftingStation craftingStation) {
        super(RPGCrafting.getPlugin(),null,craftingStation.getTitle(),new String[]{
                "         ",
                " iiiiiii ",
                " iiiiiii ",
                " iiiiiii ",
                " iiiiiii ",
                "  c   p  "
        });
        GuiElementGroup group = new GuiElementGroup('i');
        craftingStation.getRecipes().forEach(recipe -> {
            group.addElement(new StaticGuiElement('i',recipe.getResult(),click->{
                guiService.getRecipeGui(recipe).show(click.getWhoClicked());
                return true;
            }));
        });
        addElement(group);

        setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));

        addElement(new StaticGuiElement('c',new ItemStack(Material.CRAFTING_TABLE),
                click->{
            new CraftingGui(craftingStation, (Player) click.getWhoClicked()).show(click.getWhoClicked());
            return true;
        },"&f合成"));

        addElement(new DynamicGuiElement('p',humanEntity->{
            return new StaticGuiElement('p',getSkull((Player) humanEntity),
                    click -> {
                        new PlayerGui(PlayerCraftingProfile.get(humanEntity.getUniqueId()), (Player) humanEntity).show(humanEntity);
                        return true;
                    }
                    ,"&f查看你的合成");
        }));
    }

    private ItemStack getSkull(Player player){
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        meta.setOwningPlayer(player);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

}
