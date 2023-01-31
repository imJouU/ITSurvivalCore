package me.jouu.itsurvivalcore.db.mysql;

import me.jouu.itsurvivalcore.ITSurvivalCore;
import me.jouu.itsurvivalcore.db.Database;
import me.jouu.itsurvivalcore.util.ULogger;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class MySQLManager {
    private final ITSurvivalCore instance = ITSurvivalCore.getInstance();

    private final ULogger uLogger = instance.getULogger();
    private final Database database = instance.getDatabase();
    private final Connection connection = database.getConnection();

    public void checkTables() {
        uLogger.sendLog("Evn", "  &aChecking tables...");
        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();

            Map<String, Map<String, String>> tables = new HashMap<>();
            tables.put("warp_locations", new HashMap<String, String>() {
                {
                    put("name", "Warp Locations");
                    put("sql", "CREATE TABLE IF NOT EXISTS warp_locations (" +
                            "id INT(64) NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
                            "name VARCHAR(255) NOT NULL, " +
                            "world_name VARCHAR(255) NOT NULL, " +
                            "x_loc DOUBLE NOT NULL, " +
                            "y_loc DOUBLE NOT NULL, " +
                            "z_loc DOUBLE NOT NULL, " +
                            "yaw FLOAT NOT NULL, " +
                            "pitch FLOAT NOT NULL)"
                    );
                }
            });
            tables.put("teleport_player_blacklist", new HashMap<String, String>() {
                {
                    put("name", "Teleport Player Blacklist");
                    put("sql", "CREATE TABLE IF NOT EXISTS teleport_player_blacklist (" +
                            "id INT(64) NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
                            "player_uuid VARCHAR(255) NOT NULL, " +
                            "player_name VARCHAR(255) NOT NULL, " +
                            "b_player_uuid VARCHAR(255) NOT NULL, " +
                            "b_player_name VARCHAR(255) NOT NULL) "
                    );
                }
            });
            tables.put("teleport_player_settings", new HashMap<String, String>() {
                {
                    put("name", "Teleport Player Settings");
                    put("sql", "CREATE TABLE IF NOT EXISTS teleport_player_settings (" +
                            "id INT(64) NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
                            "player_uuid VARCHAR(255) NOT NULL, " +
                            "player_name VARCHAR(255) NOT NULL, " +
                            "request_status BOOLEAN NOT NULL DEFAULT 1) "
                    );
                }
            });
            tables.put("teleport_player_whitelist", new HashMap<String, String>() {
                {
                    put("name", "Teleport Player Whitelist");
                    put("sql", "CREATE TABLE IF NOT EXISTS teleport_player_whitelist (" +
                            "id INT(64) NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
                            "player_uuid VARCHAR(255) NOT NULL, " +
                            "player_name VARCHAR(255) NOT NULL, " +
                            "w_player_uuid VARCHAR(255) NOT NULL, " +
                            "w_player_name VARCHAR(255) NOT NULL) "
                    );
                }
            });
            tables.put("quest_system", new HashMap<String, String>() {
                {
                    put("name", "Quest System");
                    put("sql", "CREATE TABLE IF NOT EXISTS quest_system (" +
                            "id INT(64) NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
                            "player_uuid VARCHAR(255) NOT NULL, " +
                            "player_name VARCHAR(255) NOT NULL, " +
                            "level INT, " +
                            "points DOUBLE)"
                    );
                }
            });

            tables.forEach((table, prop) -> {
                String tableName = prop.get("name");
                String tableSql = prop.get("sql");

                uLogger.sendLog("", "    &fTable: &b" + tableName);
                try {
                    ResultSet resultSet = databaseMetaData.getTables(null, null, table, new String[] {"TABLE"});
                    if (!resultSet.next()) {
                        uLogger.sendLog("", "      &fStatus: &cNot Created!");
                        createTable(tableSql);

                        return;
                    }

                    uLogger.sendLog("", "      &fStatus: &2Created and Loaded.");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createTable(String sql) {
        uLogger.sendLog("Evn", "        &aCreating table...");
        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);
            statement.close();

            uLogger.sendLog("Inf", "          &2Created!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
