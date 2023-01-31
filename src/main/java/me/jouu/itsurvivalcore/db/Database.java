package me.jouu.itsurvivalcore.db;

import lombok.Getter;
import me.jouu.itsurvivalcore.ITSurvivalCore;
import me.jouu.itsurvivalcore.db.mysql.MySQLManager;
import me.jouu.itsurvivalcore.util.ULogger;

import java.sql.*;
import java.util.Objects;

public class Database {
    private final ITSurvivalCore instance = ITSurvivalCore.getInstance();

    private final ULogger uLogger = instance.getULogger();

    private @Getter Connection connection;

    public void initialize() {
        uLogger.sendLog("", "");
        uLogger.sendLog("Evn", "&aInitializing database... &ePlease wait! &c<3");

        // Check if a connection already exists.
        if (connection == null) {
            String databaseMethod = instance.getConfig().getString("storage.type");
            uLogger.sendLog("Inf", "  &fStorage type: &b" + databaseMethod);

            if (Objects.equals(databaseMethod, "MongoDB")) {
                connectMongoDB();

                return;
            }

            connectMySQLOrMariaDB();
            instance.setMySQLManager(new MySQLManager());
            instance.getMySQLManager().checkTables();
        }
    }

    private void connectMySQLOrMariaDB() {
        uLogger.sendLog("Evn", "  &aConnecting...");
        try {
            String address = "jdbc:mysql://"
                    + instance.getConfig().getString("storage.settings.address")
                    + "/"
                    + instance.getConfig().getString("storage.settings.database");
            String username = instance.getConfig().getString("storage.settings.username");
            String password = instance.getConfig().getString("storage.settings.password");

            connection = DriverManager.getConnection(address, username, password);

            uLogger.sendLog("Inf", "    &2Connected!");
        } catch (SQLException e) {
            uLogger.sendLog("Err", "  &cAn error has occurred while trying to connect to the database!");

            throw new RuntimeException(e);
        }
    }

    private void connectMongoDB() {}
}
