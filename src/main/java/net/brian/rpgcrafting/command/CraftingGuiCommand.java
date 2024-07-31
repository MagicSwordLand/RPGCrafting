package net.brian.rpgcrafting.command;

import dev.reactant.reactant.core.component.Component;
import dev.reactant.reactant.core.dependency.injection.Inject;
import net.brian.rpgcrafting.service.CraftingService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.management.PlatformLoggingMXBean;

@Component
public class CraftingGuiCommand extends SubCommand{

    @Inject
    CraftingService craftingService;

    protected CraftingGuiCommand() {
        super("craft");
    }

    // /crafting craft <id> player

    @Override
    protected void onCommand(CommandSender sender, String[] args) {
        if(args.length >=3){
            Player target = Bukkit.getPlayer(args[2]);
            craftingService.openStation(args[1],target);
        }
    }

}
