package net.awakaxis.scrollable_bundles;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.awakaxis.scrollable_bundles.network.ScrollBundleC2S;
import net.minecraft.util.Identifier;

public class ScrollableBundles implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Scrollable Bundles");

    public static Identifier id(String path) {
        return new Identifier("scrollable_bundles", path);
    }

    @Override
    public void onInitialize(ModContainer mod) {
        LOGGER.info("Hello Quilt world from {}! Stay fresh!", mod.metadata().name());
        ServerPlayNetworking.registerGlobalReceiver(ScrollBundleC2S.ID, new ScrollBundleC2S.Receiver());
    }
}