package net.awakaxis.scrollable_bundles;

import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public class Util {
    

    public static void scrollBundle(ItemStack bundle, int scrollDir) {
        if (!(bundle.getItem() instanceof BundleItem)) {throw new IllegalArgumentException("ItemStack must be a bundle");};
        
        NbtCompound nbt = bundle.getOrCreateNbt();
        if (!nbt.contains("Items")) {return;}
        NbtList items = nbt.getList("Items", NbtList.COMPOUND_TYPE);
        if (items.size() == 1) {return;}
        NbtList newItems = new NbtList();
        if (scrollDir > 0) {
            // scrolled up
            newItems.add(items.get(1));
            for (int i = 2; i < items.size(); i++) {
                newItems.add(items.get(i));
            }
            newItems.add(items.get(0));
        } else {
            // scrolled down
            newItems.add(items.get(items.size() - 1));
            for (int i = 0; i < items.size() - 1; i++) {
                newItems.add(items.get(i));
            }
        }
        nbt.put("Items", newItems);
    }
}
