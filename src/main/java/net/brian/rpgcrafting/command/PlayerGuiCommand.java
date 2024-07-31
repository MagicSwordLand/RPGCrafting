package net.brian.rpgcrafting.command;

import dev.reactant.reactant.core.component.Component;
import net.brian.rpgcrafting.guis.PlayerGui;
import net.brian.rpgcrafting.player.PlayerCraftingProfile;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Component
public class PlayerGuiCommand extends SubCommand{

    public PlayerGuiCommand() {
        super("player");
    }

    // /crafting player <player>
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(args.length>=2){
            Player player = Bukkit.getPlayer(args[1]);
            if(player != null){
                new PlayerGui(PlayerCraftingProfile.get(player.getUniqueId()),player).show(player);
            }

        }
    }
}
