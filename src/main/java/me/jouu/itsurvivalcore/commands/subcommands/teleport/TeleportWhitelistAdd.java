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

import java.util.List;

public class TeleportWhitelistAdd implements ITeleportInterface {
    private final ITSurvivalCore instance = ITSurvivalCore.getInstance();

    private final UMessage uMessage = instance.getUMessage();
    private final MTeleportManager mTeleportManager = instance.getMTeleportManager();

    private final FileConfiguration lang = instance.getMFileManager().getUserLangConfig();

    @Override
    public String getSubcommand() {
        return "whitelist add";
    }

    /*
      Command structure:
        /teleport   whitelist add   player
     */
    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
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
            noArgsMessageLines = lang.getStringList("modules.teleport.subcommands.whitelist.add.no-args");
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
            uMessage.playerMessage(player, lang.getString("modules.teleport.subcommands.whitelist.add.target-same-player"));

            return true;
        }

        //
        // Check if target is already on player's whitelist
        boolean found = mTeleportManager.findTargetInPlayerWhitelistByUUID(player.getUniqueId().toString(), target.getUniqueId().toString());
        if (found) {
            uMessage.playerMessage(player, lang.getString("modules.teleport.subcommands.whitelist.add.already-on-list"));

            return true;
        }

        uMessage.playerMessage(player, lang.getString("modules.teleport.subcommands.whitelist.add.adding"));

        MTeleportModel mTeleportModel = new MTeleportModel(player, target, null);
        mTeleportManager.addTargetToPlayerWhitelist(mTeleportModel);

        uMessage.playerMessage(player, lang.getString("modules.teleport.subcommands.whitelist.add.success").replace("{name}", target.getName()));
        return true;
    }
}
