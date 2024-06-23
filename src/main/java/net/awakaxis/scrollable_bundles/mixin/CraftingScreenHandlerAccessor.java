package net.awakaxis.scrollable_bundles.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.screen.CraftingScreenHandler;

@Mixin(CraftingScreenHandler.class)
public interface CraftingScreenHandlerAccessor {
    
    @Accessor("input")
    RecipeInputInventory getInput();
}
