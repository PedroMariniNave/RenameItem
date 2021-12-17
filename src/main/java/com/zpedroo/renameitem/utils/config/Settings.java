package com.zpedroo.renameitem.utils.config;

import com.zpedroo.renameitem.utils.FileUtils;
import org.bukkit.ChatColor;

import java.util.List;

public class Settings {

    public static final String COMMAND = FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.command");

    public static final List<String> ALIASES = FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Settings.aliases");

    public static final String ADMIN_PERMISSION = FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.admin-permission");

    public static final String PERMISSION_MESSAGE = ChatColor.translateAlternateColorCodes('&', FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.permission-message"));

    public static final Integer CHARACTER_LIMIT = FileUtils.get().getInt(FileUtils.Files.CONFIG, "Settings.character-limit");
}