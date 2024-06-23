package net.awakaxis.scrollable_bundles.network;

import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

import io.netty.buffer.Unpooled;
import net.awakaxis.scrollable_bundles.ScrollableBundles;
import net.awakaxis.scrollable_bundles.Util;
import net.awakaxis.scrollable_bundles.mixin.CraftingScreenHandlerAccessor;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.payload.CustomPayload;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ScrollBundleC2S {
    public static Identifier ID = ScrollableBundles.id("scroll_bundle_c2s");

    public static void send(int syncId, int slot, int scrollDir, boolean playerInv) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(syncId); 
        buf.writeInt(slot);
        buf.writeInt(scrollDir);
        buf.writeBoolean(playerInv);
        ClientPlayNetworking.send(ID, buf);
    }

    public static class Receiver implements ServerPlayNetworking.ChannelReceiver {

        @Override
        public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                PacketByteBuf buf, PacketSender<CustomPayload> responseSender) {
            server.execute(() -> {
                int syncId = buf.readInt();
                int slot = buf.readInt();
                int scrollDir = buf.readInt();
                boolean playerInv = buf.readBoolean();
                player.updateLastActionTime();
                if (player.currentScreenHandler.syncId != syncId) {
                    ScrollableBundles.LOGGER.warn("Mismatched sync id in scroll bundle packet");
                    return;
                }
                if (!player.currentScreenHandler.isValidSlotIndex(slot)) {
                    ScrollableBundles.LOGGER.warn("Invalid slot index in scroll bundle packet");
                    return;
                }
                if (!player.currentScreenHandler.canUse(player)) {
                    ScrollableBundles.LOGGER.warn("Player cannot use container in scroll bundle packet");
                    return;
                }
                ItemStack stack = ItemStack.EMPTY;
                if (playerInv) {
                    stack = player.getInventory().getStack(slot);
                } else {
                    if (player.currentScreenHandler instanceof CraftingScreenHandler) {
                        CraftingScreenHandlerAccessor accessor = (CraftingScreenHandlerAccessor) player.currentScreenHandler;
                        stack = accessor.getInput().getStack(slot);
                    } else {
                        stack = player.currentScreenHandler.getSlot(slot).getStack();
                    }
                }
                if (stack.isEmpty()) {
                    ScrollableBundles.LOGGER.warn("Empty stack in scroll bundle packet");
                    return;
                }
                if (!(stack.getItem() instanceof BundleItem)) {
                    ScrollableBundles.LOGGER.warn("Item in slot is not a bundle in scroll bundle packet");
                    return;
                }
                Util.scrollBundle(stack, scrollDir);
            });
        }
        
    }
}
