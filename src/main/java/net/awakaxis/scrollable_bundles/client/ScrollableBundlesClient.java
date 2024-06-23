package net.awakaxis.scrollable_bundles.client;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.screen.api.client.ScreenMouseEvents;

import net.awakaxis.scrollable_bundles.ScrollableBundles;
import net.awakaxis.scrollable_bundles.Util;
import net.awakaxis.scrollable_bundles.mixin.HandledScreenAccessor;
import net.awakaxis.scrollable_bundles.network.ScrollBundleC2S;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;

public class ScrollableBundlesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient(ModContainer mod) {
        ScrollableBundles.LOGGER.info("Client loaded!");

        ScreenMouseEvents.AFTER_MOUSE_SCROLL.register((screen, mouseX, mouseY, scrollDistanceX, scrollDistanceY) -> {
            if (screen instanceof HandledScreen) {
                HandledScreenAccessor accessor = (HandledScreenAccessor) screen;
                if (accessor.getFocusedSlot() != null && accessor.getFocusedSlot().hasStack()) {
                    ItemStack stack = accessor.getFocusedSlot().getStack();
                    if (stack.getItem() instanceof BundleItem) {
                        Util.scrollBundle(stack, (int) scrollDistanceY);
                        boolean playerInv = accessor.getFocusedSlot().inventory == screen.getClient().player.getInventory();
                        ScrollBundleC2S.send(accessor.getHandler().syncId, accessor.getFocusedSlot().getIndex(), (int) scrollDistanceY, playerInv);
                        ScrollableBundles.LOGGER.info("scrolled on focused slot with: " + accessor.getFocusedSlot().getStack().getItem().getName());
                        ScrollableBundles.LOGGER.info(screen.toString());
                        ScrollableBundles.LOGGER.info("slot: " + accessor.getFocusedSlot().getIndex());
                        ScrollableBundles.LOGGER.info("slots count: " + accessor.getHandler().slots.size());
                    }
                }
            }
        });
    }
    
}
