package com.zpedroo.renameitem.commands;

import com.zpedroo.renameitem.managers.InventoryManager;
import com.zpedroo.renameitem.utils.config.Messages;
import com.zpedroo.renameitem.utils.item.Item;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RenameItemCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 3) {
            switch (args[0].toUpperCase()) {
                case "GIVE":
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage(Messages.OFFLINE_PLAYER);
                        return true;
                    }

                    Integer amount = null;
                    try {
                        amount = Integer.parseInt(args[2]);
                    } catch (Exception ex) {
                        // ignore
                    }

                    if (amount == null || amount <= 0) {
                        sender.sendMessage(Messages.INVALID_AMOUNT);
                        return true;
                    }

                    InventoryManager.getInstance().giveItem(target, Item.get().build(amount));
                    return true;
            }
        }

        sender.sendMessage(Messages.COMMAND_USAGE);
        return false;
    }
}