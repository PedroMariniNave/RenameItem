package com.zpedroo.renameitem.listeners;

import com.zpedroo.renameitem.managers.InventoryManager;
import com.zpedroo.renameitem.utils.config.Messages;
import com.zpedroo.renameitem.utils.config.Settings;
import com.zpedroo.renameitem.utils.item.Item;
import de.tr7zw.nbtapi.NBTItem;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class PlayerGeneralListeners implements Listener {

    private static Map<Player, ItemStack> renamingItem;

    static {
        renamingItem = new HashMap<>(8);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        if (!renamingItem.containsKey(event.getPlayer())) return;

        event.setCancelled(true);

        Player player = event.getPlayer();
        ItemStack item = renamingItem.remove(player);

        if (event.getMessage().toCharArray().length > Settings.CHARACTER_LIMIT) {
            player.sendMessage(Messages.CHARACTER_LIMIT);
            InventoryManager.getInstance().giveItem(player, item);
            InventoryManager.getInstance().giveItem(player, Item.get().build(1));
            return;
        }

        String name = ChatColor.translateAlternateColorCodes('&', event.getMessage());

        NBTItem nbt = new NBTItem(item);
        if (nbt.hasKey("StatTrakKills")) {
            int kills = nbt.getInteger("StatTrakKills");
            name = name + " " + StringUtils.replace(com.zpedroo.stattrak.utils.config.Settings.DISPLAY, "{kills}", String.valueOf(kills));
        }

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);

        InventoryManager.getInstance().giveItem(player, item);
        player.playSound(player.getLocation(), Sound.ANVIL_USE, 0.2f, 10f);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null || event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) return;
        if (event.getCursor() == null || event.getCursor().getType().equals(Material.AIR)) return;
        if (event.getClickedInventory().getType() != InventoryType.PLAYER) return;

        Player player = (Player) event.getWhoClicked();
        if (player.getGameMode().equals(GameMode.CREATIVE)) return;

        ItemStack cursor = event.getCursor().clone();
        NBTItem cursorNBT = new NBTItem(cursor);

        if (!cursorNBT.hasNBTData()) return;
        if (!cursorNBT.hasKey("RenameItem")) return;
        if (event.getAction() != InventoryAction.SWAP_WITH_CURSOR) return;

        event.setCancelled(true);

        if (renamingItem.containsKey(player)) {
            ItemStack item = renamingItem.remove(player);

            InventoryManager.getInstance().giveItem(player, item);
            InventoryManager.getInstance().giveItem(player, Item.get().build(1));
        }

        ItemStack currentItem = event.getCurrentItem();
        renamingItem.put(player, currentItem.clone());

        event.setCurrentItem(new ItemStack(Material.AIR));
        event.setCursor(new ItemStack(Material.AIR));

        if (cursor.getAmount() > 1) {
            cursor.setAmount(cursor.getAmount() - 1);
            InventoryManager.getInstance().giveItem(player, cursor);
        }

        player.updateInventory();
        player.closeInventory();

        for (int i = 0; i < 25; ++i) {
            player.sendMessage("");
        }

        for (String msg : Messages.RENAMING_ITEM) {
            if (msg == null) continue;

            player.sendMessage(msg);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        if (!renamingItem.containsKey(event.getPlayer())) return;

        Player player = event.getPlayer();
        ItemStack item = renamingItem.remove(player);

        InventoryManager.getInstance().giveItem(player, item);
        InventoryManager.getInstance().giveItem(player, Item.get().build(1));
    }

    public static Map<Player, ItemStack> getRenamingItem() {
        return renamingItem;
    }
}