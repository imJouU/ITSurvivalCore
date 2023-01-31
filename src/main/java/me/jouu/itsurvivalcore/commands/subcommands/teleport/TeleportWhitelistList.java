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

public class TeleportWhitelistList implements ITeleportInterface {
    private final ITSurvivalCore instance = ITSurvivalCore.getInstance();

    private final UMessage uMessage = instance.getUMessage();
    private final MTeleportManager mTeleportManager = instance.getMTeleportManager();

    private final FileConfiguration lang = instance.getMFileManager().getUserLangConfig();

    @Override
    public String getSubcommand() {
        return "whitelist list";
    }

    /*
      Command structure:
        /teleport   whitelist list
        /teleport   whitelist list   player
     */
    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        Player target;
        List<String> noArgsMessageLines;
        List<String> listOfPlayers;
        List<String> listOfPlayersMessageLines;

        String adminPermission = "itscore.teleport.whitelist.admin";
        String cmdPermission = "itscore.teleport.whitelist.list";
        String cmdPermissionOthers = "itscore.teleport.whitelist.list.others";

        /////////////////////////
        //  Sender is console  //
        /////////////////////////
        if (commandSender instanceof ConsoleCommandSender) {
            //
            // Check arguments
            if (args.length == 2) {
                noArgsMessageLines = lang.getStringList("modules.teleport.subcommands.whitelist.list.no-args");
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
            uMessage.consoleMessage(lang.getString("modules.teleport.subcommands.whitelist.list.looking"));
            listOfPlayers = mTeleportManager.getListOfPlayersOnTheWhitelist(target.getUniqueId().toString());

            if (listOfPlayers.size() > 0) {
                listOfPlayersMessageLines = lang.getStringList("modules.teleport.subcommands.whitelist.list.success");
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
            uMessage.consoleMessage(lang.getString("modules.teleport.subcommands.whitelist.list.no-players-found"));
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
            uMessage.consoleMessage(lang.getString("modules.teleport.subcommands.whitelist.list.looking"));
            listOfPlayers = mTeleportManager.getListOfPlayersOnTheWhitelist(target.getUniqueId().toString());

            if (listOfPlayers.size() > 0) {
                listOfPlayersMessageLines = lang.getStringList("modules.teleport.subcommands.whitelist.list.success");
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
            uMessage.consoleMessage(lang.getString("modules.teleport.subcommands.whitelist.list.no-players-found"));
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
        uMessage.consoleMessage(lang.getString("modules.teleport.subcommands.whitelist.list.looking"));
        listOfPlayers = mTeleportManager.getListOfPlayersOnTheWhitelist(player.getUniqueId().toString());

        if (listOfPlayers.size() > 0) {
            listOfPlayersMessageLines = lang.getStringList("modules.teleport.subcommands.whitelist.list.success");
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
        uMessage.consoleMessage(lang.getString("modules.teleport.subcommands.whitelist.list.no-players-found"));
        return true;
    }
}
