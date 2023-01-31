package me.jouu.itsurvivalcore.commands.subcommands.teleport;

import me.jouu.itsurvivalcore.ITSurvivalCore;
import me.jouu.itsurvivalcore.interfaces.ITeleportInterface;
import me.jouu.itsurvivalcore.managers.MTeleportManager;
import me.jouu.itsurvivalcore.util.UMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class TeleportBlacklistList implements ITeleportInterface {
    private final ITSurvivalCore instance = ITSurvivalCore.getInstance();

    private final UMessage uMessage = instance.getUMessage();
    private final MTeleportManager mTeleportManager = instance.getMTeleportManager();

    private final FileConfiguration lang = instance.getMFileManager().getUserLangConfig();

    @Override
    public String getSubcommand() {
        return "blacklist list";
    }

    /*
      Command structure:
        /teleport   blacklist list
        /teleport   blacklist list   player
     */
    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        Player target;
        List<String> noArgsMessageLines;
        List<String> listOfPlayers;
        List<String> listOfPlayersMessageLines;

        String adminPermission = "itscore.teleport.blacklist.admin";
        String cmdPermission = "itscore.teleport.blacklist.list";
        String cmdPermissionOthers = "itscore.teleport.blacklist.list.others";

        /////////////////////////
        //  Sender is console  //
        /////////////////////////
        if (commandSender instanceof ConsoleCommandSender) {
            //
            // Check arguments
            if (args.length == 2) {
                noArgsMessageLines = lang.getStringList("modules.teleport.subcommands.blacklist.list.no-args");
                noArgsMessageLines.forEach(uMessage::consoleMessage);

                return true;
            }

            //
            // Get target and check it
            target = Bukkit.getPlayerExact(args[2]);
            if (target == null) {
                uMessage.consoleMessage(lang.getString("player-not-online"));

                return true;
            }

            //
            // Get list of players
            uMessage.consoleMessage(lang.getString("modules.teleport.subcommands.blacklist.list.looking"));
            listOfPlayers = mTeleportManager.getListOfBlacklistedPlayers(target.getUniqueId().toString());

            if (listOfPlayers.size() > 0) {
                listOfPlayersMessageLines = lang.getStringList("modules.teleport.subcommands.blacklist.list.success");
                for (String line : listOfPlayersMessageLines) {
                    if (!line.contains("{id}") && !line.contains("{name}")) {
                        uMessage.consoleMessage(line.replace("{amount}", Integer.toString(listOfPlayers.size())));

                        continue;
                    }

                    for (int i = 1; i <= listOfPlayers.size(); i++) uMessage.consoleMessage(line.replace("{id}", Integer.toString(i)).replace("{name}", listOfPlayers.get(i - 1)));
                }

                return true;
            }

            //
            // No players found
            uMessage.consoleMessage(lang.getString("modules.teleport.subcommands.blacklist.list.no-players-found"));
            return true;
        }



        ////////////////////////
        //  Sender is player  //
        ////////////////////////
        Player player = (Player) commandSender;

        //
        // Target data
        if (args.length == 3) {
            //
            // Check permissions
            if (!player.hasPermission(adminPermission) || !player.hasPermission(cmdPermissionOthers)) {
                uMessage.playerMessage(player, lang.getString("no-permission"));

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
            // Get list of players
            uMessage.playerMessage(player, lang.getString("modules.teleport.subcommands.blacklist.list.looking"));
            listOfPlayers = mTeleportManager.getListOfBlacklistedPlayers(target.getUniqueId().toString());

            if (listOfPlayers.size() > 0) {
                listOfPlayersMessageLines = lang.getStringList("modules.teleport.subcommands.blacklist.list.success");
                for (String line : listOfPlayersMessageLines) {
                    if (!line.contains("{id}") && !line.contains("{name}")) {
                        uMessage.playerMessage(player, line.replace("{amount}", Integer.toString(listOfPlayers.size())));

                        continue;
                    }

                    for (int i = 1; i <= listOfPlayers.size(); i++) uMessage.playerMessage(player, line.replace("{id}", Integer.toString(i)).replace("{name}", listOfPlayers.get(i - 1)));
                }

                return true;
            }

            //
            // No players found
            uMessage.playerMessage(player, lang.getString("modules.teleport.subcommands.blacklist.list.no-players-found"));
            return true;
        }

        //
        // Check permissions
        if (!player.hasPermission(adminPermission) || !player.hasPermission(cmdPermission)) {
            uMessage.playerMessage(player, lang.getString("no-permission"));

            return true;
        }

        //
        // Get list of players
        uMessage.playerMessage(player, lang.getString("modules.teleport.subcommands.blacklist.list.looking"));
        listOfPlayers = mTeleportManager.getListOfBlacklistedPlayers(player.getUniqueId().toString());

        if (listOfPlayers.size() > 0) {
            listOfPlayersMessageLines = lang.getStringList("modules.teleport.subcommands.blacklist.list.success");
            for (String line : listOfPlayersMessageLines) {
                if (!line.contains("{id}") && !line.contains("{name}")) {
                    uMessage.playerMessage(player, line.replace("{amount}", Integer.toString(listOfPlayers.size())));

                    continue;
                }

                for (int i = 1; i <= listOfPlayers.size(); i++) uMessage.playerMessage(player, line.replace("{id}", Integer.toString(i)).replace("{name}", listOfPlayers.get(i - 1)));
            }

            return true;
        }

        //
        // No players found
        uMessage.playerMessage(player, lang.getString("modules.teleport.subcommands.blacklist.list.no-players-found"));
        return true;
    }
}
