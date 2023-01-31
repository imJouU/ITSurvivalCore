package me.jouu.itsurvivalcore.commands;

import me.jouu.itsurvivalcore.ITSurvivalCore;
import me.jouu.itsurvivalcore.interfaces.ITeleportInterface;
import me.jouu.itsurvivalcore.managers.MTeleportManager;
import me.jouu.itsurvivalcore.util.UMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class Teleport implements CommandExecutor {
    private final ITSurvivalCore instance = ITSurvivalCore.getInstance();

    private final UMessage uMessage = instance.getUMessage();
    private final MTeleportManager mTeleportManager = instance.getMTeleportManager();

    private final FileConfiguration lang = instance.getMFileManager().getUserLangConfig();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        /*
          Player commands:
               Command     Subcommands         Argument
               /teleport                       player
               /teleport   accept              player       IN PROGRESS...
               /teleport   deny                player
               /teleport   toggle                           DONE :D
               /teleport   blacklist add       player       DONE :D
               /teleport   blacklist remove    player       DONE :D
               /teleport   blacklist list                   DONE :D
               /teleport   whitelist add       player       DONE :D
               /teleport   whitelist remove    player       DONE :D
               /teleport   whitelist list                   DONE :D

          Console commands:
               Command     Subcommands         Argument
               /teleport   blacklist list      player       DONE :D
               /teleport   whitelist list      player       DONE :D
         */


        //
        // Format subcommand
        String subcommand;
        if (args.length >= 2) subcommand = String.join(" ", Arrays.asList(args).subList(0, 2));
        else subcommand = String.join(" ", args);

        //
        // Sender is console
        if (sender instanceof ConsoleCommandSender) {
            //
            // No subcommand provided or command does not exist.
            if (subcommand.length() == 0 || !mTeleportManager.getSubcommands().containsKey(subcommand)) {
                sendConsoleAvailableCommands();

                return true;
            }

            //
            // Check if command is available to use it.
            if (!subcommand.equalsIgnoreCase("blacklist list") && !subcommand.equalsIgnoreCase("whitelist list")) {
                String langMessage = lang.getString("console-messages.command-not-available");
                uMessage.consoleMessage(langMessage);

                return true;
            }

            //
            // Run command
            ITeleportInterface iTeleportInterface = mTeleportManager.getSubcommands().get(subcommand);
            return iTeleportInterface.execute(sender, args);
        }

        //
        // Sender is player
        Player player = (Player) sender;

        uMessage.playerMessage(player, "Argument: " + subcommand);

        if (subcommand.length() == 0 || !mTeleportManager.getSubcommands().containsKey(subcommand)) {
            if (player.hasPermission("itscode.teleport.admin")) {
                sendAdminAvailableCommands(player);
            } else {
                sendPlayerAvailableCommands(player);
            }

            return true;
        }

        ITeleportInterface iTeleportInterface = mTeleportManager.getSubcommands().get(subcommand);
        return iTeleportInterface.execute(sender, args);
    }

    private void sendConsoleAvailableCommands() {
        lang.getStringList("modules.teleport.console-available-commands").forEach(uMessage::consoleMessage);
    }

    private void sendAdminAvailableCommands(Player player) {
        lang.getStringList("modules.teleport.admin-available-commands").forEach(line -> uMessage.playerMessage(player, line));
    }

    private void sendPlayerAvailableCommands(Player player) {
        lang.getStringList("modules.teleport.player-available-commands").forEach(line -> uMessage.playerMessage(player, line));
    }
}
