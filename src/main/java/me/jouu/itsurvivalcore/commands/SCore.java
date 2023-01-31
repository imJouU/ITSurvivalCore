package me.jouu.itsurvivalcore.commands;

import me.jouu.itsurvivalcore.ITSurvivalCore;
import me.jouu.itsurvivalcore.managers.MTeleportManager;
import me.jouu.itsurvivalcore.util.ULogger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

public class SCore implements CommandExecutor {
    private final ITSurvivalCore instance = ITSurvivalCore.getInstance();
    private final ULogger uLogger = instance.getULogger();

    private final MTeleportManager mTeleportManager = instance.getMTeleportManager();

    // score
    // score reload

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 0) return true;

            if (args[0].equalsIgnoreCase("add")) {
                String uuid = "8f60894e-34c8-45f0-919f-d53b85584312";
                String name = "Test";
                String uuid2 = "c86c808f-5b9f-47ff-b83d-2c028ee79eec";
                String name2 = "Blocked_Player";

//                if (mTeleportManager.findBlacklistedPlayerByUUID(uuid2)) {
//                    uLogger.sendLog("Err", "&cThis player is already blacklisted!");
//
//                    return true;
//                }

                // MTeleportBL mTeleportBL = new MTeleportBL(UUID.fromString(uuid), name, UUID.fromString(uuid2), name2);
                // mTeleportManager.addPlayerToBlacklist(sender, mTeleportBL);

                return true;
            }
            return true;
        }
        return true;
    }
}
