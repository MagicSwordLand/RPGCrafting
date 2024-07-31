package net.brian.rpgcrafting.command;

import dev.reactant.reactant.core.component.lifecycle.LifeCycleHook;
import dev.reactant.reactant.core.dependency.injection.Inject;
import org.bukkit.command.CommandSender;

public abstract class SubCommand implements LifeCycleHook {

    @Inject
    CraftingCommandManager commandManager;

    String name;
    protected SubCommand(String name){
        this.name = name;
    }


    @Override
    public void onEnable(){
        commandManager.register(this);
    }

    public String getName() {
        return name;
    }

    protected abstract void onCommand(CommandSender sender, String[] args);


}
