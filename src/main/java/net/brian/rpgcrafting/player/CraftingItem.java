package net.brian.rpgcrafting.player;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.stat.type.ItemStat;
import net.brian.playerdatasync.data.gson.PostProcessable;
import net.brian.rpgcrafting.RPGCrafting;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;

public class CraftingItem implements PostProcessable {

    String name;
    final long duration,timestamp;
    UUID playerUUID;

    int taskID;

    String serializedItem;
    transient ItemStack itemStack;

    public CraftingItem(Player player, ItemStack itemStack, long duration){
        timestamp = System.currentTimeMillis();
        this.playerUUID = player.getUniqueId();
        this.duration = duration;
        this.itemStack = itemStack;
        serializedItem = ItemStackBase64.getString(itemStack);
        taskID=Bukkit.getScheduler().runTaskLater(RPGCrafting.getPlugin(),()->{
            if(player.isOnline()) player.sendMessage("你的"+name+"以合成完畢");
        },duration/1000*20).getTaskId();

        if(itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()){
            name = itemStack.getItemMeta().getDisplayName();
        }
        else name = "";
    }

    @Override
    public void gsonPostSerialize() {
        Bukkit.getScheduler().cancelTask(taskID);
    }

    @Override
    public void gsonPostDeserialize() {
        Player player = Bukkit.getPlayer(playerUUID);
        if(player != null){
            long delay = duration - (System.currentTimeMillis() - timestamp);
            if(delay > 0){
                taskID=Bukkit.getScheduler().runTaskLater(RPGCrafting.getPlugin(),()->{
                    player.sendMessage("你的"+name+"以合成完畢");
                },(duration-(System.currentTimeMillis()-timestamp))/1000*20).getTaskId();
            }
            else {
                player.sendMessage("你的"+name+"以合成完畢");
            }
        }
    }

    public ItemStack getResult() {
        return itemStack;
    }

    public String getName() {
        return name;
    }

    public boolean craftComplete(){
        return System.currentTimeMillis() - timestamp > duration;
    }

    static final SimpleDateFormat format = new SimpleDateFormat("HH小時m分鐘s秒");

    public String getRemainingTime(){
        long remain = duration-(System.currentTimeMillis()-timestamp)-28800000;
        Date date = new Date(remain);
        return format.format(date);
    }

}
