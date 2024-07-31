package net.brian.rpgcrafting.configs;

import net.brian.playerdatasync.util.IridiumColorAPI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CraftingStation {

    String title;
    List<CraftingRecipe> craftingRecipes = new ArrayList<>();

    public void setUp(){
        craftingRecipes.forEach(CraftingRecipe::setUp);
        title = IridiumColorAPI.process(title);
    }

    public Optional<CraftingRecipe> findReceipt(ItemStack[] ingredients, Player player){
        return craftingRecipes.stream().filter(receipt-> receipt.canCraft(ingredients,player)).findFirst();
    }

    public String getTitle() {
        return title;
    }

    public List<CraftingRecipe> getRecipes() {
        return craftingRecipes;
    }
}
