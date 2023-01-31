package me.jouu.itsurvivalcore.commands.subcommands.teleport;

import me.jouu.itsurvivalcore.ITSurvivalCore;
import me.jouu.itsurvivalcore.interfaces.ITeleportInterface;
import me.jouu.itsurvivalcore.managers.MTeleportManager;
import me.jouu.itsurvivalcore.model.teleport.MTeleportModel;
import me.jouu.itsurvivalcore.util.UMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TeleportBlacklistAdd implements ITeleportInterface {
    private final ITSurvivalCore instance = ITSurvivalCore.getInstance();

    private final UMessage uMessage = instance.getUMessage();
    private final MTeleportManager mTeleportManager = instance.getMTeleportManager();

    private final FileConfiguration lang = instance.getMFileManager().getUserLangConfig();

    @Override
    public String getSubcommand() {
        return "blacklist add";
    }

    /*
      Command structure:
        /teleport   blacklist add   player
     */
    @Override
    public boolean execute(CommandSender commandSender, String @NotNull [] args) {
        Player player = (Player) commandSender;
        Player target;
        List<String> noArgsMessageLines;

        String adminPermission = "itscore.teleport.whitelist.admin";
        String cmdPermission = "itscore.teleport.whitelist.add";

        //
        // Check permissions
        if (!player.hasPermission(adminPermission) || !player.hasPermission(cmdPermission)) {
            uMessage.playerMessage(player, lang.getString("no-permission"));

            return true;
        }

        //
        // Check arguments
        if (args.length == 2) {
            noArgsMessageLines = lang.getStringList("modules.teleport.subcommands.blacklist.add.no-args");
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
            uMessage.playerMessage(player, lang.getString("modules.teleport.subcommands.blacklist.add.target-same-player"));

            return true;
        }

        //
        // Check if target is already on player's blacklist
        boolean found = mTeleportManager.findTargetInPlayerBlacklistByUUID(player.getUniqueId().toString(), target.getUniqueId().toString());
        if (found) {
            uMessage.playerMessage(player, lang.getString("modules.teleport.subcommands.blacklist.add.already-on-list"));

            return true;
        }

        uMessage.playerMessage(player, lang.getString("modules.teleport.subcommands.blacklist.add.adding"));

        MTeleportModel mTeleportModel = new MTeleportModel(player, target, null);
        mTeleportManager.addTargetToPlayerBlacklist(mTeleportModel);

        uMessage.playerMessage(player, lang.getString("modules.teleport.subcommands.blacklist.add.success").replace("{name}", target.getName()));
        return true;
    }
}
