package com.zpedroo.renameitem.utils.item;

import com.zpedroo.renameitem.utils.FileUtils;
import com.zpedroo.renameitem.utils.builder.ItemBuilder;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

public class Item {

    private static Item instance;
    public static Item get() { return instance; }

    private ItemStack item;

    public Item() {
        instance = this;
        this.item = ItemBuilder.build(FileUtils.get().getFile(FileUtils.Files.CONFIG).get(), "Item").build();
    }

    public ItemStack build(Integer amount) {
        item.setAmount(amount);

        NBTItem nbt = new NBTItem(item);
        nbt.addCompound("RenameItem");

        return nbt.getItem();
    }
}