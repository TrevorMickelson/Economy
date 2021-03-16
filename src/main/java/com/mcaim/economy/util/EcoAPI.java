package com.mcaim.economy.util;

import com.mcaim.core.util.Util;
import com.mcaim.economy.EcoMain;
import org.bukkit.Bukkit;

import java.util.UUID;

/**
 * Every method is designed to work with
 * online and offline players, if the player
 * is offline, they'll get stored to avoid
 * spam calls to data base
 */
public class EcoAPI {
    private static final EcoMain plugin = EcoMain.getInstance();

    public static Double getBalance(UUID uuid) {
        if (plugin.getEcoManager().containsUser(uuid))
            return Util.round(plugin.getEcoManager().getBalance(uuid), 2);

        double offlineBal = plugin.getDataBase().getBalance(uuid);
        plugin.getEcoManager().addUser(uuid, offlineBal);
        return Util.round(offlineBal, 2);
    }

    public static void addBalance(UUID uuid, double amount) {
        double balance = getBalance(uuid);
        plugin.getEcoManager().updateBalance(uuid, balance + amount);
    }

    public static void removeBalance(UUID uuid, Double amount) {
        double balance = getBalance(uuid);
        plugin.getEcoManager().updateBalance(uuid, balance - amount);
    }

    public static void setBalance(UUID uuid, double amount) {
        if (Bukkit.getPlayer(uuid) != null) {
            plugin.getEcoManager().updateBalance(uuid, amount);
            return;
        }

        double balance = plugin.getDataBase().getBalance(uuid);
        plugin.getEcoManager().addUser(uuid, balance);
    }

    public static String getBalanceFormatted(UUID uuid) {
        return Util.formatNumber(getBalance(uuid));
    }
}
