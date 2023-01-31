package me.jouu.itsurvivalcore.interfaces;

import org.bukkit.command.CommandSender;

public interface ITeleportInterface {
    String getSubcommand();

    boolean execute(CommandSender commandSender, String[] args);
}
