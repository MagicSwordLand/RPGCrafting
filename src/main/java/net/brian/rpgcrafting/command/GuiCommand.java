package net.brian.rpgcrafting.command;

import dev.reactant.reactant.core.component.Component;
import dev.reactant.reactant.core.dependency.injection.Inject;
import net.brian.rpgcrafting.configs.CraftingStation;
import net.brian.rpgcrafting.guis.*;
import net.brian.rpgcrafting.player.PlayerCraftingProfile;
import net.brian.rpgcrafting.service.CraftingService;
import net.brian.rpgcrafting.service.GuiService;
import net.brian.rpgcrafting.service.UpgradeService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Component
public class GuiCommand extends SubCommand{

    @Inject
    UpgradeService upgradeService;

    @Inject
    GuiService guiService;

    @Inject
    CraftingService craftingService;

    @Inject
    BrewingGui brewingGui;

    public GuiCommand() {
        super("gui");
    }

    @Override
    public void onEnable(){
        super.onEnable();
    }

    // /crafting gui <gui> player <id>

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(!sender.hasPermission("crafting.admin")){
            return;
        }
        if(args.length<3){
            return;
        }

        String gui = args[1];
        Player player = Bukkit.getPlayer(args[2]);
        if(player == null) return;

        switch (gui){
            case "craft":
                if(args[3] != null){
                    CraftingStation station = craftingService.getStation(args[3]);
                    if(station == null){
                        sender.sendMessage("Can't find station");
                    }
                    else new CraftingGui(station,player).show(player);
                }
                break;
            case "player":
                PlayerGui playerGui = new PlayerGui(PlayerCraftingProfile.get(player.getUniqueId()),player);
                playerGui.show(player);
                break;
            case "station":
                if(args[3] != null) {
                    try {
                        guiService.getStationGui(args[3]).show(player);
                    } catch (NullPointerException ignore) {
                        player.sendMessage("找不到該Station");
                    }
                    break;
                }
            case "upgrade":
                new UpgradeGui(upgradeService,player).show(player);
                break;
            case "brew":
                brewingGui.show(player);

        }
    }
}
