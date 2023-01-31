package me.jouu.itsurvivalcore.util;

import me.jouu.itsurvivalcore.ITSurvivalCore;
import me.jouu.itsurvivalcore.enums.ELogger;
import org.jetbrains.annotations.NotNull;

public class ULogger {
    private final ITSurvivalCore instance = ITSurvivalCore.getInstance();
    private final UMessage uMessage = instance.getUMessage();

    public void sendLog(@NotNull String id, String message) {
        switch (id) {
            case "Deb":
                uMessage.consoleMessage(ELogger.Debug.getLog() + message);
                break;
            case "Evn":
                uMessage.consoleMessage(ELogger.Event.getLog() + message);
                break;
            case "Inf":
                uMessage.consoleMessage(ELogger.Info.getLog() + message);
                break;
            case "Err":
                uMessage.consoleMessage(ELogger.Error.getLog() + message);
                break;
            case "Ftl":
                uMessage.consoleMessage(ELogger.Fatal.getLog() + message);
                break;
            case "Wrn":
                uMessage.consoleMessage(ELogger.Warning.getLog() + message);
                break;
            default:
                uMessage.consoleMessage(ELogger.System.getLog() + message);
                break;
        }
    }
}
