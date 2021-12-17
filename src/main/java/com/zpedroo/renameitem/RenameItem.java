package com.zpedroo.renameitem;

import com.zpedroo.renameitem.commands.RenameItemCmd;
import com.zpedroo.renameitem.listeners.PlayerGeneralListeners;
import com.zpedroo.renameitem.managers.InventoryManager;
import com.zpedroo.renameitem.utils.FileUtils;
import com.zpedroo.renameitem.utils.item.Item;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

import static com.zpedroo.renameitem.utils.config.Settings.*;

public class RenameItem extends JavaPlugin {

    private static RenameItem instance;
    public static RenameItem get() { return instance; }

    public void onEnable() {
        instance = this;
        new FileUtils(this);
        new InventoryManager();
        new Item();

        registerCommand(COMMAND, ALIASES, ADMIN_PERMISSION, PERMISSION_MESSAGE, new RenameItemCmd());
        registerListeners();
    }

    public void onDisable() {
        for (Map.Entry<Player, ItemStack> entry : PlayerGeneralListeners.getRenamingItem().entrySet()) {
            Player player = entry.getKey();
            ItemStack item = entry.getValue();

            InventoryManager.getInstance().giveItem(player, item);
            InventoryManager.getInstance().giveItem(player, Item.get().build(1));
        }
    }

    private void registerCommand(String command, List<String> aliases, String permission, String permissionMessage, CommandExecutor executor) {
        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);

            PluginCommand pluginCmd = constructor.newInstance(command, this);
            pluginCmd.setAliases(aliases);
            pluginCmd.setExecutor(executor);
            pluginCmd.setPermission(permission);
            pluginCmd.setPermissionMessage(permissionMessage);

            Field field = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap commandMap = (CommandMap) field.get(Bukkit.getPluginManager());
            commandMap.register(getName().toLowerCase(), pluginCmd);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerGeneralListeners(), this);
    }
}