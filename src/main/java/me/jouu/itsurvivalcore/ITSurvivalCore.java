package me.jouu.itsurvivalcore;

import lombok.Getter;
import lombok.Setter;
import me.jouu.itsurvivalcore.db.Database;
import me.jouu.itsurvivalcore.db.mysql.MySQLManager;
import me.jouu.itsurvivalcore.managers.MFileManager;
import me.jouu.itsurvivalcore.managers.MModuleManager;
import me.jouu.itsurvivalcore.managers.MTeleportManager;
import me.jouu.itsurvivalcore.util.ULogger;
import me.jouu.itsurvivalcore.util.UMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class ITSurvivalCore extends JavaPlugin {
    private static @Getter ITSurvivalCore instance;

    private @Getter UMessage uMessage;
    private @Getter ULogger uLogger;

    private @Getter MFileManager mFileManager;

    private @Getter Database database;
    private @Getter @Setter MySQLManager mySQLManager;

    private @Getter MModuleManager mModuleManager;
    private @Getter @Setter MTeleportManager mTeleportManager;

    @Override
    public void onEnable() {
        instance = this;
        uMessage = new UMessage();
        uLogger = new ULogger();


        sendEnableInfoMessage();


        //////////////////
        //  Load files  //
        //////////////////
        mFileManager = new MFileManager();
        mFileManager.loadFiles();



        ///////////////////////////
        //  Initialize database  //
        //////////////////
        String databaseMethod = instance.getConfig().getString("storage.type");
        if (!isValidStorageType(databaseMethod)) {
            uLogger.sendLog("Err", "&cThe storage type in the configuration is invalid...");
            uLogger.sendLog("Err", "&cDisabling...");
            Bukkit.getPluginManager().disablePlugin(this);

            return;
        }
        database = new Database();
        database.initialize();



        //////////////////////
        //  Enable modules  //
        //////////////////////
        mModuleManager = new MModuleManager();
        mModuleManager.initialize();


        // Register events
        // ????

        // Check profile key
        // ?????

        // Objects.requireNonNull(getServer().getPluginCommand("score")).setExecutor(new SCore());
        // Objects.requireNonNull(getServer().getPluginCommand("teleport")).setExecutor(new Teleport());
    }

    @Override
    public void onDisable() {
        uLogger.sendLog("", "");
        uLogger.sendLog("", "&cPlugin disabled!");
        uLogger.sendLog("", "");
    }



    private void sendEnableInfoMessage() {
        ChatColor chatColor1 = generateRandomChatColor();
        ChatColor chatColor2 = generateRandomChatColor();
        uLogger.sendLog("", "");
        uLogger.sendLog("", chatColor1 + " ______     __  __     ______     __   __   __     __   __   ______     __");
        uLogger.sendLog("", chatColor1 + "/\\  ___\\   /\\ \\/\\ \\   /\\  == \\   /\\ \\ / /  /\\ \\   /\\ \\ / /  /\\  __ \\   /\\ \\");
        uLogger.sendLog("", chatColor1 + "\\ \\___  \\  \\ \\ \\_\\ \\  \\ \\  __<   \\ \\ \\'/   \\ \\ \\  \\ \\ \\'/   \\ \\  __ \\  \\ \\ \\____");
        uLogger.sendLog("", chatColor1 + " \\/\\_____\\  \\ \\_____\\  \\ \\_\\ \\_\\  \\ \\__|    \\ \\_\\  \\ \\__|    \\ \\_\\ \\_\\  \\ \\_____\\");
        uLogger.sendLog("", chatColor1 + "  \\/_____/   \\/_____/   \\/_/ /_/   \\/_/      \\/_/   \\/_/      \\/_/\\/_/   \\/_____/");
        uLogger.sendLog("", chatColor2 + " ______     ______     ______     ______");
        uLogger.sendLog("", chatColor2 + "/\\  ___\\   /\\  __ \\   /\\  == \\   /\\  ___\\");
        uLogger.sendLog("", chatColor2 + "\\ \\ \\____  \\ \\ \\/\\ \\  \\ \\  __<   \\ \\  __\\");
        uLogger.sendLog("", chatColor2 + " \\ \\_____\\  \\ \\_____\\  \\ \\_\\ \\_\\  \\ \\_____\\");
        uLogger.sendLog("", chatColor2 + "  \\/_____/   \\/_____/   \\/_/ /_/   \\/_____/");
        uLogger.sendLog("", "");
        uLogger.sendLog("", "");
        uLogger.sendLog("", "&fVersion: &b1.0");
        uLogger.sendLog("", "&fProject: &dAstral");
        uLogger.sendLog("", "&fDevelopment Team: &eiLuxiion Team");
        uLogger.sendLog("", "&fChangelogs: &bhttps://google.com/");
        uLogger.sendLog("", "");
        uLogger.sendLog("", "");
    }

    private ChatColor generateRandomChatColor() {
        ChatColor[] colors = ChatColor.values();
        ChatColor[] textColors = new ChatColor[colors.length];
        int index = 0;
        for (ChatColor color : colors) {
            if (color.isColor()) {
                textColors[index] = color;
                index++;
            }
        }
        Random r = new Random();
        return textColors[r.nextInt(index)];
    }

    private boolean isValidStorageType(String type) {
        Set<String> validStorageTypes = new HashSet<>(Arrays.asList("MySQL", "MariaDB", "MongoDB"));

        return validStorageTypes.contains(type);
    }
}
