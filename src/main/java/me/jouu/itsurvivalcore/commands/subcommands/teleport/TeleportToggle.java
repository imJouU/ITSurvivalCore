package me.jouu.itsurvivalcore.commands.subcommands.teleport;

import me.jouu.itsurvivalcore.ITSurvivalCore;
import me.jouu.itsurvivalcore.interfaces.ITeleportInterface;
import me.jouu.itsurvivalcore.managers.MTeleportManager;
import me.jouu.itsurvivalcore.util.UMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class TeleportToggle implements ITeleportInterface {
    private final ITSurvivalCore instance = ITSurvivalCore.getInstance();

    private final UMessage uMessage = instance.getUMessage();
    private final MTeleportManager mTeleportManager = instance.getMTeleportManager();

    private final FileConfiguration lang = instance.getMFileManager().getUserLangConfig();

    @Override
    public String getSubcommand() {
        return "toggle";
    }

    /*
      Command structure:
        /teleport   toggle
     */
    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        Player player = (Player) commandSender;

        /*
        uMessage.playerMessage(player, "");
        uMessage.playerMessage(player, "");

        uMessage.playerMessage(player, "&f&lTOGGLE MESSAGES&r");
        uMessage.playerMessage(player, "&eYour &brequest receipt status &ehas been &aenabled&e.");
        uMessage.playerMessage(player, "&eYour &brequest receipt status &ehas been &cdisabled&e.");

        uMessage.playerMessage(player, "");

        uMessage.playerMessage(player, "&f&lBLACKLIST MESSAGES&r");
        uMessage.playerMessage(player, "&8Adding player...");
        uMessage.playerMessage(player, "&aimJouU &ahas been added to your blacklist!");
        uMessage.playerMessage(player, "&cThis player is already on your blacklist.");
        uMessage.playerMessage(player, "&cYou can not add yourself to your blacklist.");
        uMessage.playerMessage(player, "&cYou can not remove yourself from your blacklist.");

        uMessage.playerMessage(player, "");

        uMessage.playerMessage(player, "&f&lWHITELIST MESSAGES&r");
        uMessage.playerMessage(player, "&8Adding player...");
        uMessage.playerMessage(player, "&aimJouU &ahas been added to your whitelist!");
        uMessage.playerMessage(player, "&cThis player is already on your whitelist.");
        uMessage.playerMessage(player, "&cYou can not add yourself to your whitelist.");
        uMessage.playerMessage(player, "&cYou can not remove yourself from your whitelist.");

        uMessage.playerMessage(player, "");

        uMessage.playerMessage(player, "&f&lDENY MESSAGES&r");
        uMessage.playerMessage(player, "&cRequest denied.");
        uMessage.playerMessage(player, "&eimJouU &chas denied your teleport request.");
        uMessage.playerMessage(player, "&cYou have no requests from this player");


        uMessage.playerMessage(player, "");

        uMessage.playerMessage(player, "&f&lACCEPT MESSAGES&r");
        uMessage.playerMessage(player, "&aRequest accepted.");
        uMessage.playerMessage(player, "&bimJouU &ewill be teleported to you in &b3 seconds&e...");
        uMessage.playerMessage(player, "&eimJouU will be teleported to you in &b3 seconds&e...");
        uMessage.playerMessage(player, "&bimJouU &ahas accepted your teleport request.");
        uMessage.playerMessage(player, "&eimJouU &ahas accepted your teleport request.");
        uMessage.playerMessage(player, "&eYou will be teleported in &b3 seconds&e...");
        uMessage.playerMessage(player, "&8Teleporting...");
        uMessage.playerMessage(player, "&cYou have no requests from this player");

        uMessage.playerMessage(player, "");

        uMessage.playerMessage(player, "&f&lGENERAL MESSAGES&r");
        uMessage.playerMessage(player, "&cYou have already sent a teleport request to this player.");
        uMessage.playerMessage(player, "&8Sending request...");
        uMessage.playerMessage(player, "&aTeleport request sent!");
        uMessage.playerMessage(player, "&eYou have sent a teleport request to &bimJouU");
        uMessage.playerMessage(player, "&aNew teleport request!");
        uMessage.playerMessage(player, "&eYou have received a teleport request from &bimJouU");
        uMessage.playerMessage(player, "&eClick &a&lACCEPT&r &eto confirm the request or click on &c&lDENY&r &eto cancel.");
        uMessage.playerMessage(player, "&8You have 60 seconds to accept this request before it is automatically cancelled.");
        uMessage.playerMessage(player, "&8Looking for information...");
        uMessage.playerMessage(player, "&cThis teleport request could not be found.");
         */

        String adminPermission = "itscode.teleport.admin";
        String cmdPermission = "itscore.teleport.toggle";

        //
        // Check permissions
        if (!player.hasPermission(adminPermission) || !player.hasPermission(cmdPermission)) {
            uMessage.playerMessage(player, lang.getString("no-permission"));

            return true;
        }

        boolean status = mTeleportManager.getRequestStatus(player.getUniqueId().toString());
        mTeleportManager.toggleRequests(player.getUniqueId().toString(), !status);

        uMessage.playerMessage(player, status ? lang.getString("modules.teleport.subcommands.toggle.status-disabled") : lang.getString("modules.teleport.subcommands.toggle.status-enabled"));
        return true;
    }
}
