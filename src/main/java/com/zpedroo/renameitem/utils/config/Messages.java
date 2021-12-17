package com.zpedroo.renameitem.utils.config;

import com.zpedroo.renameitem.utils.FileUtils;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Messages {

    public static final String COMMAND_USAGE = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.command-usage"));

    public static final String OFFLINE_PLAYER = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.offline-player"));

    public static final String INVALID_AMOUNT = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.invalid-amount"));

    public static final String CHARACTER_LIMIT = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.character-limit"));

    public static final List<String> RENAMING_ITEM = getColored(FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Messages.renaming-item"));

    private static String getColored(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    private static List<String> getColored(List<String> list) {
        List<String> colored = new ArrayList<>(list.size());
        for (String str : list) {
            colored.add(getColored(str));
        }

        return colored;
    }
}