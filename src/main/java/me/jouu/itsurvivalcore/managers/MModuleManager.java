package me.jouu.itsurvivalcore.managers;

import me.jouu.itsurvivalcore.ITSurvivalCore;
import me.jouu.itsurvivalcore.util.ULogger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MModuleManager {
    private final ITSurvivalCore instance = ITSurvivalCore.getInstance();

    private final ULogger uLogger = instance.getULogger();

    private final List<String> listOfModules = new ArrayList<>();

    public void initialize() {
        uLogger.sendLog("", "");
        uLogger.sendLog("Evn", "&aInitializing modules... &ePlease wait! &c<3");

        FileConfiguration config = instance.getConfig();

        ConfigurationSection configurationSection = config.getConfigurationSection("modules");
        if (configurationSection == null) {
            uLogger.sendLog("Err", "  &cAn error has occurred while trying to initialize modules!");
            uLogger.sendLog("Err", "  &cSection &emodules &cdoes not exist in the configuration.");

            return;
        }

        for (Map.Entry<String, Object> module : configurationSection.getValues(false).entrySet()) {
            String moduleName = module.getKey();
            listOfModules.add(moduleName);

            boolean isModuleEnabled = ((ConfigurationSection) module.getValue()).getBoolean("enabled");
            if (moduleName.equalsIgnoreCase("teleport") && isModuleEnabled) {
                instance.setMTeleportManager(new MTeleportManager());
                instance.getMTeleportManager().enable();
            }
        }
    }



    public List<String> getListOfModules() { return listOfModules; }
}
