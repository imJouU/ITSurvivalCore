package me.jouu.itsurvivalcore.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class UMessage {
    public void playerMessage(Player player, String message) {
        if (player == null) return;

        player.sendMessage(coloredMessage(message));
    }

    public void consoleMessage(String message) {
        ConsoleCommandSender consoleCommandSender = Bukkit.getConsoleSender();

        consoleCommandSender.sendMessage(coloredMessage(message));
    }

    private String coloredMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
