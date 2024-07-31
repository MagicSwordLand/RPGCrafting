package net.brian.rpgcrafting.configs;

import io.lumine.mythic.lib.api.item.NBTItem;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.Objects;
import java.util.logging.Level;

public class CraftingRecipe {

    public String mmoType,mmoID;

    String command="";
    float cost=0;
    public long duration;
    LinkedList<String> recipe = new LinkedList<>();

    transient Ingredient[] ingredients = new Ingredient[9];

    public void setUp(){
        int i = 0;
        for (String s : recipe) {
            if(!s.equals("")){
                String[] args = s.split(":");
                ingredients[i] = new Ingredient(args[0].toUpperCase(),args[1].toUpperCase(),Integer.parseInt(args[2]));
            }
            i++;
        }
    }

    public static class Ingredient{
        public Ingredient(String type,String id,int amount){
            this.id = id;
            this.type = type;
            this.amount = amount;
        }
        public String type,id;
        public int amount;

        public String id() {
            return id;
        }

        public int amount() {
            return amount;
        }
    }

    public boolean canCraft(ItemStack[] itemStacks,Player player){
        if(itemStacks.length == 9){
            int i = 0;
            for (ItemStack itemStack : itemStacks) {
                if(ingredients[i] == null){
                    if(itemStack != null && !itemStack.getType().equals(Material.AIR)){
                        return false;
                    }
                }
                else {
                    NBTItem nbtItem = NBTItem.get(itemStack);
                    if(!canUse(nbtItem,player)){
                        return false;
                    }
                    String type = nbtItem.getString("MMOITEMS_ITEM_TYPE");
                    String id = nbtItem.getString("MMOITEMS_ITEM_ID");
                    if (type != null && type.equals(ingredients[i].type)){
                        if(id.equals(ingredients[i].id)){
                            if(itemStack.getAmount() < ingredients[i].amount){
                                return false;
                            };
                        }
                        else return false;
                    }
                    else return false;
                }
                i++;
            }
            return checkMoney(player,cost);
        }
        else Bukkit.getLogger().log(Level.INFO, ChatColor.RED + "配方過短");
        return false;
    }

    private boolean checkMoney(Player player,float amount){
        Economy economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
        if(economy.getBalance(player)>=amount){
            return true;
        }
        player.sendMessage("你沒有足夠的錢 你需要"+amount+"$");
        return false;
    }

    public ItemStack getResult(){
        return MMOItems.plugin.getItem(mmoType,mmoID);
    }

    String path = "MMOITEMS_SOULBELONG";
    boolean canUse(NBTItem item,Player player){
        if(item.hasTag(path) && item.getString(path).contains(player.getPlayer().getUniqueId().toString())){
            player.sendMessage("無法使用他人的靈魂賦予物品合成");
            return false;
        }
        return true;
    }

    public ItemStack[] getIngredientItems(){
        ItemStack[] items = new ItemStack[9];
        int i = 0;
        for (Ingredient ingredient : ingredients) {
            if(ingredient != null){
                ItemStack itemStack = MMOItems.plugin.getItem(ingredient.type,ingredient.id);
                if(itemStack != null){
                    itemStack.setAmount(ingredient.amount);
                    items[i] = itemStack;
                }
            }
            i++;
        }
        return items;
    }

    public Ingredient[] getIngredients() {
        return ingredients;
    }

    public float getCost() {
        return cost;
    }

    public String getName(){
        ItemStack itemStack = getResult();
        if(itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()){
            return itemStack.getItemMeta().getDisplayName();
        }
        return itemStack.getType().name();
    }

    public String getCommand(Player player){
        return command.replace("%player%",player.getName());
    }

}
