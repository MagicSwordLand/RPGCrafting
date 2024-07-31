package net.brian.rpgcrafting.guis;

import dev.reactant.reactant.core.component.Component;
import net.brian.rpgcrafting.RPGCrafting;
import net.brian.rpgcrafting.guis.element.InventoryGui;

@Component
public class BrewingGui extends InventoryGui {

    public BrewingGui() {
        super(RPGCrafting.getPlugin(),"",new String[]{
                "aaaaaaaaa"
        });
    }
}
