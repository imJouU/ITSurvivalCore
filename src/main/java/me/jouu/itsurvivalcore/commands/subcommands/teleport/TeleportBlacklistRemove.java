package me.jouu.itsurvivalcore.commands.subcommands.teleport;

import me.jouu.itsurvivalcore.ITSurvivalCore;
import me.jouu.itsurvivalcore.interfaces.ITeleportInterface;
import me.jouu.itsurvivalcore.managers.MTeleportManager;
import me.jouu.itsurvivalcore.util.UMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class TeleportBlacklistRemove implements ITeleportInterface {
    private final ITSurvivalCore instance = ITSurvivalCore.getInstance();

    private final UMessage uMessage = instance.getUMessage();
    private final MTeleportManager mTeleportManager = instance.getMTeleportManager();

    private final FileConfiguration lang = instance.getMFileManager().getUserLangConfig();

    @Override
    public String getSubcommand() {
        return "blacklist remove";
    }

    /*
      Command structure:
        /teleport   blacklist remove   player
     */
    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        Player player = (Player) commandSender;
        Player target;
        List<String> noArgsMessageLines;

        String adminPermission = "itscore.teleport.blacklist.admin";
        String cmdPermission = "itscore.teleport.blacklist.remove";

        //
        // Check permissions
        if (!player.hasPermission(adminPermission) || !player.hasPermission(cmdPermission)) {
            uMessage.playerMessage(player, lang.getString("no-permission"));

            return true;
        }

        //
        // Check arguments
        if (args.length == 2) {
            noArgsMessageLines = lang.getStringList("modules.teleport.subcommands.blacklist.remove.no-args");
            noArgsMessageLines.forEach(line -> uMessage.playerMessage(player, line));

            return true;
        }

        //
        // Get target and check it
        target = Bukkit.getPlayerExact(args[2]);
        if (target == null) {
            uMessage.playerMessage(player, lang.getString("player-not-online"));

            return true;
        }

        //
        // Check if target is the same player
        if (target.equals(player)) {
            uMessage.playerMessage(player, lang.getString("modules.teleport.subcommands.blacklist.remove.target-same-player"));

            return true;
        }

        //
        // Check if target is already on player's blacklist
        boolean found = mTeleportManager.findTargetInPlayerBlacklistByUUID(player.getUniqueId().toString(), target.getUniqueId().toString());
        if (!found) {
            uMessage.playerMessage(player, lang.getString("modules.teleport.subcommands.blacklist.remove.not-on-list"));

            return true;
        }

        uMessage.playerMessage(player, lang.getString("modules.teleport.subcommands.blacklist.remove.removing"));

        mTeleportManager.removeTargetFromPlayerBlacklist(player.getUniqueId().toString(), target.getUniqueId().toString());
        uMessage.playerMessage(player, lang.getString("modules.teleport.subcommands.blacklist.remove.success").replace("{name}", target.getName()));
        return true;
    }
}
