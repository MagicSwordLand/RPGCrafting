package net.brian.rpgcrafting.guis;

import net.brian.rpgcrafting.RPGCrafting;
import net.brian.rpgcrafting.configs.CraftingRecipe;
import net.brian.rpgcrafting.configs.CraftingStation;
import net.brian.rpgcrafting.guis.element.*;
import net.brian.rpgcrafting.player.CraftingItem;
import net.brian.rpgcrafting.player.PlayerCraftingProfile;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CraftingGui extends net.brian.rpgcrafting.guis.element.InventoryGui {

    public CraftingGui(CraftingStation station, Player player) {
        super(RPGCrafting.getPlugin(),null, station.getTitle(), new String[]{
                "         ",
                " iii     ",
                " iii c o ",
                " iii     ",
                "         ",
                "    b    "
        });
        Inventory inv = Bukkit.createInventory(null, InventoryType.CHEST);
        addElement(new GuiStorageElement('i', inv));
        StaticGuiElement checkCrafting = new StaticGuiElement('c',new ItemStack(Material.GREEN_STAINED_GLASS_PANE),
                click -> {
                    ItemStack[] ingredients = new ItemStack[9];
                    for(int i=0;i<9;i++){
                        ingredients[i] = inv.getItem(i);
                    }
                    station.findReceipt(ingredients,player).ifPresent(recipe->{
                        StaticGuiElement element = ((StaticGuiElement)click.getElement());
                        ItemStack itemStack = recipe.getResult();
                        element.setItem(itemStack);
                        if(itemStack.hasItemMeta()&&itemStack.getItemMeta().hasLore()){
                            List<String> lore = itemStack.getItemMeta().getLore();
                            String[] arr = new String[lore.size()+1];
                            arr[0] = itemStack.getItemMeta().getDisplayName();
                            for(int i=1;i<=lore.size();i++){
                                arr[i] = lore.get(i-1);
                            }
                            element.setText(arr);
                        }
                        draw();
                    });
                    return true;
                },
                "&a查看可能結果");
        addElement(checkCrafting);
        addElement(new StaticGuiElement('o',new ItemStack(Material.YELLOW_STAINED_GLASS_PANE),
                click -> {
                    ItemStack[] ingredients = new ItemStack[9];
                    for(int i=0;i<9;i++){
                        ingredients[i] = inv.getItem(i);
                    }
                    station.findReceipt(ingredients,player).ifPresent(recipe->{
                        ItemStack result = recipe.getResult();
                        if(result != null && !result.getType().equals(Material.AIR)){
                            PlayerCraftingProfile.get(player.getUniqueId()).craftingItems
                                    .add(new CraftingItem(player,result,recipe.duration));
                            player.sendMessage("成功合成"+recipe.getName()+"可至個人合成表查看");
                            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE,1,1);
                            takeMoney(player,recipe.getCost());
                            takeItems(ingredients,recipe.getIngredients());
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),recipe.getCommand(player));
                            draw();
                        }
                    });
                    return true;
                },"&a嘗試合成"));
        setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        addElement(new StaticGuiElement('b',new ItemStack(Material.ARROW),
                click -> {
                    Arrays.stream(inv.getContents()).filter(Objects::nonNull).forEach(item->{
                        if(!player.getInventory().addItem(item).isEmpty()){
                            player.getWorld().dropItem(player.getLocation(),item,entity->{
                                entity.setOwner(player.getUniqueId());
                                player.sendMessage("你的包包滿了，合成素材已掉到地上");
                            });
                        }
                    });
                    goBack(player);
                    Bukkit.getScheduler().runTaskLater(RPGCrafting.getPlugin(), this::destroy,5L);
                    return true;
                }));
        setCloseAction(close -> {
            Arrays.stream(inv.getContents()).filter(Objects::nonNull).forEach(item->{
                if(!player.getInventory().addItem(item).isEmpty()){
                    player.getWorld().dropItem(player.getLocation(),item,entity->{
                        entity.setOwner(player.getUniqueId());
                        player.sendMessage("你的包包滿了，合成素材已掉到地上");
                    });
                }
            });
            Bukkit.getScheduler().runTaskLater(RPGCrafting.getPlugin(), this::destroy,5L);
            return false;
        });
    }

    private void takeMoney(Player player,float amount){
        Economy economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
        economy.withdrawPlayer(player,amount);
    }

    private void takeItems(ItemStack[] ingredients, CraftingRecipe.Ingredient[] costs){
        for(int i=0;i<9;i++){
            if(ingredients[i] != null){
                ingredients[i].setAmount(ingredients[i].getAmount()-costs[i].amount());
            }
        }
    }



}
