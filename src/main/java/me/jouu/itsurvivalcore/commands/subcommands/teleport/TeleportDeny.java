package me.jouu.itsurvivalcore.commands.subcommands.teleport;

import me.jouu.itsurvivalcore.interfaces.ITeleportInterface;
import org.bukkit.command.CommandSender;

public class TeleportDeny implements ITeleportInterface {
    @Override
    public String getSubcommand() {
        return null;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        return false;
    }
}
