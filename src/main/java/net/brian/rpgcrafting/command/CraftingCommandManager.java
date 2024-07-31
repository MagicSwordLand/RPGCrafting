package net.brian.rpgcrafting.command;

import dev.reactant.reactant.core.component.Component;
import dev.reactant.reactant.core.component.lifecycle.LifeCycleHook;
import net.brian.rpgcrafting.RPGCrafting;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CraftingCommandManager implements CommandExecutor, LifeCycleHook {

    List<SubCommand> subCommands = new ArrayList<>();

    @Override
    public void onEnable(){
        RPGCrafting.getPlugin().getCommand("crafting").setExecutor(this);
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length > 0){
            getCommand(args[0]).ifPresent(subCommand -> {
                subCommand.onCommand(sender,args);
            });
            return true;
        }
        sender.sendMessage("錯誤指令用法");
        return true;
    }

    public void register(SubCommand subCommand){
        subCommands.add(subCommand);
    }

    private Optional<SubCommand> getCommand(String name){
        return subCommands.stream()
                .filter(subCommand -> subCommand.getName().startsWith(name))
                .findFirst();
    }

}
