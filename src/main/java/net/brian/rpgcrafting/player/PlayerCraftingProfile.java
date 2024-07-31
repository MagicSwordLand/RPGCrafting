package net.brian.rpgcrafting.player;


import net.brian.playerdatasync.PlayerDataSync;
import net.brian.playerdatasync.data.PlayerData;
import net.brian.playerdatasync.data.gson.PostProcessable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerCraftingProfile extends PlayerData implements PostProcessable {


    public List<CraftingItem> craftingItems = new ArrayList<>();


    public PlayerCraftingProfile(UUID uuid) {
        super(uuid);

    }

    public static PlayerCraftingProfile get(UUID uuid){
        return PlayerDataSync.getInstance().getData(uuid,PlayerCraftingProfile.class).get();
    }

    @Override
    public void gsonPostDeserialize(){
        craftingItems.removeIf(craftingItem -> craftingItem.getResult() == null);
    }

}
