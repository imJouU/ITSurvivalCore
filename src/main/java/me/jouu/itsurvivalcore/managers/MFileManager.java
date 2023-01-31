package me.jouu.itsurvivalcore.managers;

import me.jouu.itsurvivalcore.ITSurvivalCore;
import me.jouu.itsurvivalcore.util.ULogger;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MFileManager {
    private final ITSurvivalCore instance = ITSurvivalCore.getInstance();
    private final ULogger uLogger = instance.getULogger();

    private FileConfiguration userLangConfig;

    private final Map<String, File> languages = new HashMap<>();

    public void loadFiles() {
        uLogger.sendLog("", "");
        uLogger.sendLog("Evn", "&aLoading files... &ePlease wait! &c<3");

        /////////////////////////
        //  General Files      //
        //  Load:              //
        //    Default config   //
        //    Lang             //
        /////////////////////////
        loadDefaultFile();

        registerLanguages();
    }



    ///////////////////////////////
    //  Main config file | load  //
    ///////////////////////////////
    private void loadDefaultFile() {
        uLogger.sendLog("", "  &fFile: &bDefault Config");
        uLogger.sendLog("Evn", "    &aLoading...");
        instance.saveDefaultConfig();
        uLogger.sendLog("Inf", "      &2Loaded!");
    }



    ////////////////////////////////////////////////////////////////
    //  Language file | reload, register, check, load and create  //
    ////////////////////////////////////////////////////////////////
    public void reloadUserLangConfig(Player player) {
        String langCode = instance.getConfig().getString("language");
        if (!languages.containsKey(langCode)) {
            // player.sendMessage(uColor.coloredMsg("&cThe language code registered in the configuration &e(" + langCode + ") &cdoes not match any file."));
            // player.sendMessage(uColor.coloredMsg("&cPlease check your configuration file..."));

            return;
        }

        File langFile = languages.get(langCode);
        String langName = langFile.getName();

        if (!langFile.exists()) createLangFile("langs/" + langName);
        try {
            userLangConfig.load(langFile);
            // player.sendMessage(uColor.coloredMsg("&fThe language file &b" + langName + " &fhas been &2reloaded!"));
        } catch (InvalidConfigurationException | IOException e) {
            // player.sendMessage(uColor.coloredMsg("&cA problem occurred while trying to load this file."));

            throw new RuntimeException(e);
        }
    }

    private void registerLanguages() {
        uLogger.sendLog("", "  &fFile: &bLanguages");
        uLogger.sendLog("Evn", "    &aRegistering languages...");

        File langFolder = new File(instance.getDataFolder(), "langs");
        languages.put("en_US", new File(langFolder, "en_US.yml"));
        languages.put("es_ES", new File(langFolder, "es_ES.yml"));
        languages.put("fr_FR", new File(langFolder, "fr_FR.yml"));
        languages.put("it_IT", new File(langFolder, "it_IT.yml"));

        uLogger.sendLog("Inf", "      &2Registered!");
        uLogger.sendLog("", "    &fLanguages: &b" + languages.size());

        checkLangFiles();
    }

    private void checkLangFiles() {
        uLogger.sendLog("Evn", "    &aChecking languages files...");

        for (Map.Entry<String, File> entry : languages.entrySet()) {
            File langFile = entry.getValue();
            String langName = langFile.getName();

            if (!langFile.exists()) createLangFile("langs/" + langName);
        }
        uLogger.sendLog("Evn", "      &2Checked!");

        String default_language = instance.getConfig().getString("language");
        loadUserLangConfig(default_language);
    }

    private void loadUserLangConfig(String langCode) {
        uLogger.sendLog("Evn", "    &aLoading language...");

        if (!languages.containsKey(langCode)) {
            uLogger.sendLog("Err", "    &cThe language code registered in the configuration &e(" + langCode + ") &cdoes not match any file.");
            uLogger.sendLog("Err", "    &cPlease check your configuration file...");

            return;
        }

        uLogger.sendLog("Inf", "      &fLanguage code: &d" + langCode);

        File langFile = languages.get(langCode);
        userLangConfig = new YamlConfiguration();
        try {
            userLangConfig.load(langFile);
            uLogger.sendLog("Inf", "      &2Loaded.");
        } catch (InvalidConfigurationException | IOException e) {
            uLogger.sendLog("Inf", "      &cA problem occurred while trying to load this file.");

            throw new RuntimeException(e);
        }
    }

    private void createLangFile(String lang) {
        uLogger.sendLog("", "      &fFile: &3" + lang);
        uLogger.sendLog("Wrn", "        &eThis file does not exist!");
        uLogger.sendLog("Evn", "          &aCreating...");
        instance.saveResource(lang, false);

        uLogger.sendLog("Inf", "            &2Created!");
    }



    /////////////////////
    //  Getting files  //
    /////////////////////
    public FileConfiguration getUserLangConfig() {
        return userLangConfig;
    }
}
