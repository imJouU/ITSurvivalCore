package me.jouu.itsurvivalcore.managers;

import me.jouu.itsurvivalcore.ITSurvivalCore;
import me.jouu.itsurvivalcore.commands.Teleport;
import me.jouu.itsurvivalcore.commands.subcommands.teleport.*;
import me.jouu.itsurvivalcore.db.Database;
import me.jouu.itsurvivalcore.interfaces.ITeleportInterface;
import me.jouu.itsurvivalcore.model.teleport.MTeleportModel;
import me.jouu.itsurvivalcore.util.ULogger;
import me.jouu.itsurvivalcore.util.UMessage;
import org.bukkit.command.PluginCommand;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MTeleportManager {
    private final ITSurvivalCore instance = ITSurvivalCore.getInstance();

    private final ULogger uLogger = instance.getULogger();
    private final UMessage uMessage = instance.getUMessage();
    private final Database database = instance.getDatabase();
    private final Connection connection = database.getConnection();

    private final Map<String, ITeleportInterface> subcommands = new HashMap<>();

    public void enable() {
        uLogger.sendLog("", "  &fModule: &bTeleport");
        uLogger.sendLog("Evn", "    &aEnabling command: &e/teleport");

        PluginCommand command = instance.getServer().getPluginCommand("teleport");
        if (command == null) {
            uLogger.sendLog("Err", "        &cOops! This command can not be found!");

            return;
        }
        command.setExecutor(new Teleport());
        uLogger.sendLog("Inf", "      &2Enabled!");

        uLogger.sendLog("Evn", "    &aEnabling subcommands...");
        subcommands.put("accept", new TeleportAccept());
        subcommands.put("deny", new TeleportDeny());
        subcommands.put("toggle", new TeleportToggle());
        subcommands.put("blacklist add", new TeleportBlacklistAdd());
        subcommands.put("blacklist remove", new TeleportBlacklistRemove());
        subcommands.put("blacklist list", new TeleportBlacklistList());
        subcommands.put("whitelist add", new TeleportWhitelistAdd());
        subcommands.put("whitelist remove", new TeleportWhitelistRemove());
        subcommands.put("whitelist list", new TeleportWhitelistList());
        uLogger.sendLog("Inf", "      &2Enabled!");
    }

    public Map<String, ITeleportInterface> getSubcommands() {
        return subcommands;
    }



    ///////////////////////
    //  Data management  //
    ///////////////////////
    //
    // Toggle
    public boolean getRequestStatus(String playerUUID) {
        String query = "SELECT request_status FROM teleport_player_settings WHERE player_uuid = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, playerUUID);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) return resultSet.getBoolean("request_status");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public void toggleRequests(String playerUUID, boolean newStatus) {
        String updateQuery = "UPDATE teleport_player_settings SET request_status = ? WHERE player_uuid = ?";

        try (PreparedStatement updatePreparedStatement = connection.prepareStatement(updateQuery)) {
            updatePreparedStatement.setBoolean(1, newStatus);
            updatePreparedStatement.setString(2, playerUUID);

            updatePreparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //
    // Blacklist
    public boolean findTargetInPlayerBlacklistByUUID(String playerUUID, String targetUUID) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String query = "SELECT player_uuid, b_player_uuid FROM teleport_player_blacklist WHERE " +
                "player_uuid = ? " +
                "AND " +
                "b_player_uuid = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, playerUUID);
            preparedStatement.setString(2, targetUUID);

            resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();

                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addTargetToPlayerBlacklist(@NotNull MTeleportModel mTeleportModel) {
        String playerUUID = mTeleportModel.getPlayer().getUniqueId().toString();
        String playerName = mTeleportModel.getPlayer().getName();
        String targetUUID = mTeleportModel.getTarget().getUniqueId().toString();
        String targetName = mTeleportModel.getTarget().getName();

        String query = "INSERT INTO teleport_player_blacklist(" +
                "player_uuid, " +
                "player_name, " +
                "b_player_uuid, " +
                "b_player_name) " +
                "VALUES " +
                "(?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, playerUUID);
            preparedStatement.setString(2, playerName);
            preparedStatement.setString(3, targetUUID);
            preparedStatement.setString(4, targetName);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeTargetFromPlayerBlacklist(String playerUUID, String targetUUID) {
        String query = "DELETE FROM teleport_player_blacklist WHERE " +
                "player_uuid = ? " +
                "AND " +
                "b_player_uuid = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, playerUUID);
            preparedStatement.setString(2, targetUUID);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getListOfBlacklistedPlayers(String playerUUID) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<String> list = new ArrayList<>();
        String query = "SELECT b_player_name FROM teleport_player_blacklist WHERE player_uuid = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, playerUUID);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String data = resultSet.getString("b_player_name");
                list.add(data);
            }

            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();

                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //
    // Whitelist
    public boolean findTargetInPlayerWhitelistByUUID(String playerUUID, String targetUUID) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String query = "SELECT player_uuid, w_player_uuid FROM teleport_player_whitelist WHERE " +
                "player_uuid = ? " +
                "AND " +
                "w_player_uuid = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, playerUUID);
            preparedStatement.setString(2, targetUUID);

            resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();

                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addTargetToPlayerWhitelist(@NotNull MTeleportModel mTeleportModel) {
        String playerUUID = mTeleportModel.getPlayer().getUniqueId().toString();
        String playerName = mTeleportModel.getPlayer().getName();
        String targetUUID = mTeleportModel.getTarget().getUniqueId().toString();
        String targetName = mTeleportModel.getTarget().getName();

        String query = "INSERT INTO teleport_player_whitelist(" +
                "player_uuid, " +
                "player_name, " +
                "w_player_uuid, " +
                "w_player_name) " +
                "VALUES " +
                "(?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, playerUUID);
            preparedStatement.setString(2, playerName);
            preparedStatement.setString(3, targetUUID);
            preparedStatement.setString(4, targetName);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeTargetFromPlayerWhitelist(String playerUUID, String targetUUID) {
        String query = "DELETE FROM teleport_player_whitelist WHERE " +
                "player_uuid = ? " +
                "AND " +
                "w_player_uuid = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, playerUUID);
            preparedStatement.setString(2, targetUUID);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getListOfPlayersOnTheWhitelist(String playerUUID) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<String> list = new ArrayList<>();
        String query = "SELECT w_player_name FROM teleport_player_whitelist WHERE player_uuid = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, playerUUID);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String data = resultSet.getString("w_player_name");
                list.add(data);
            }

            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();

                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
