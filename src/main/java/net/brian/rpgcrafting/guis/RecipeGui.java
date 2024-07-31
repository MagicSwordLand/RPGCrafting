package net.brian.rpgcrafting.guis;

import net.brian.rpgcrafting.RPGCrafting;
import net.brian.rpgcrafting.configs.CraftingRecipe;
import net.brian.rpgcrafting.guis.element.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class RecipeGui extends net.brian.rpgcrafting.guis.element.InventoryGui {

    public RecipeGui(CraftingRecipe recipe) {
        super(RPGCrafting.getPlugin(),recipe.getName(),new String[]{
                "fffffffff",
                "f012fffff",
                "f345fcfof",
                "f678fffff",
                "fffffffff",
                "ffffbffff"
        });
        addElement(new StaticGuiElement('f',new ItemStack(Material.GRAY_STAINED_GLASS_PANE),
                " "));
        ItemStack[] ingredients = recipe.getIngredientItems();
        for(int i=0;i<9;i++){
            if(ingredients[i] != null){
                addElement(new StaticGuiElement((char)(i+'0'),ingredients[i]));
            }
        }
        addElement(new StaticGuiElement('c',new ItemStack(Material.GOLD_INGOT),"&6"+recipe.getCost()+"$"));
        addElement(new GuiBackElement('b',new ItemStack(Material.ARROW),true,"&f返回"));
        addElement(new StaticGuiElement('o',recipe.getResult()));
    }




}
