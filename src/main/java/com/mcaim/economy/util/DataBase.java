package com.mcaim.economy.util;

import com.mcaim.core.mysql.MySQL;
import com.mcaim.economy.EcoMain;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.UUID;

public class DataBase {
    public void createTable() {
        try {
            PreparedStatement statement = MySQL.getStatement("CREATE TABLE IF NOT EXISTS economy "
                    + "(UUID VARCHAR(100), BALANCE DOUBLE, PRIMARY KEY (UUID))");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBalance(UUID uuid, double balance) {
        // Not storing a player unless
        // they actually have a balance
        if (balance <= 0)
            return;

        try {
            if (!MySQL.uuidExists(uuid, "economy"))
                MySQL.addUuid(uuid, "economy");

            PreparedStatement statement = MySQL.getStatement("UPDATE economy SET BALANCE=? WHERE UUID=?");
            statement.setDouble(1, balance);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getBalance(UUID uuid) {
        try {
            PreparedStatement statement = MySQL.getStatement("SELECT BALANCE FROM economy WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
                return resultSet.getDouble("BALANCE");

            return 0.0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0.0;
    }

    public void updateTopTenPlayers() {
        LinkedHashMap<UUID, Double> topTenUsers = new LinkedHashMap<>();

        try {
            PreparedStatement statement = MySQL.getStatement("SELECT * FROM economy ORDER BY BALANCE DESC");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                UUID uuid = UUID.fromString(resultSet.getString("UUID"));
                Double balance = resultSet.getDouble("BALANCE");
                topTenUsers.put(uuid, balance);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        EcoMain.getInstance().getEcoManager().setTopTenUsers(topTenUsers);
    }
}
