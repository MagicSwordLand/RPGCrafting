package net.brian.rpgcrafting.command;

import dev.reactant.reactant.core.component.Component;
import dev.reactant.reactant.core.dependency.injection.Inject;
import net.brian.rpgcrafting.configs.CraftingStation;
import net.brian.rpgcrafting.guis.StationGui;
import net.brian.rpgcrafting.service.CraftingService;
import net.brian.rpgcrafting.service.GuiService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Component
public class StationGuiCommand extends SubCommand {

    @Inject
    CraftingService craftingService;

    @Inject
    GuiService guiService;

    public StationGuiCommand() {
        super("recipe");

    }

    // /crafting recipe <id> player
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(args.length>=3){
            Player player = Bukkit.getPlayer(args[2]);
            if(player != null){
                StationGui stationGui = guiService.getStationGui(args[1]);
                if(stationGui != null){
                    stationGui.show(player);
                }
            }
        }
    }
}
