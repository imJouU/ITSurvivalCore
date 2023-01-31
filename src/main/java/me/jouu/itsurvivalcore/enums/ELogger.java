package me.jouu.itsurvivalcore.enums;

import lombok.Getter;

public enum ELogger {
    System("&bITSurvivalCore &8| "),
    Debug("&3ITSurvivalCore &8| "),
    Event("&5ITSurvivalCore &8| "),
    Info("&2ITSurvivalCore &8| "),
    Error("&cITSurvivalCore &8| "),
    Fatal("&4ITSurvivalCore &8| "),
    Warning("&eITSurvivalCore &8| ");

    private final @Getter String log;

    ELogger(String log) {
        this.log = log;
    }
}
