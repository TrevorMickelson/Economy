package com.mcaim.economy;

import com.mcaim.core.scheduler.Async;
import com.mcaim.economy.commands.*;
import com.mcaim.economy.listeners.BankNoteListener;
import com.mcaim.economy.listeners.JoinLeaveListener;
import com.mcaim.economy.util.DataBase;
import com.mcaim.economy.util.EcoAPI;
import com.mcaim.economy.util.EcoManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class EcoMain extends JavaPlugin {
    private static EcoMain ecoMain;
    public static EcoMain getInstance() { return ecoMain; }

    private EcoManager ecoManager;
    public EcoManager getEcoManager() { return ecoManager; }

    private DataBase dataBase;
    public DataBase getDataBase() { return dataBase; }

    @Override
    public void onEnable() {
        ecoMain = this;
        ecoManager = new EcoManager();
        dataBase = new DataBase();
        dataBase.createTable();
        loadOnlinePlayers();
        registerCommands();
        registerListeners();
        updateOnlinePlayers();
        updateTopTenPlayers();
    }

    @Override
    public void onDisable() {
        saveOnlinePlayers();
    }

    private void registerCommands() {
        new BalanceCmd().registerCommand();
        new EcoCmd().registerCommand();
        new BalanceTopCmd().registerCommand();
        new PayCmd().registerCommand();
        new WithdrawCmd().registerCommand();
        new ForceUpdateCmd().registerCommand();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new JoinLeaveListener(), this);
        getServer().getPluginManager().registerEvents(new BankNoteListener(), this);
    }

    /**
     * If the plugin is reloaded or loaded
     * while players are online, loading their
     * balances
     */
    private void loadOnlinePlayers() {
        Async.get().run(() -> {
            Bukkit.getOnlinePlayers().forEach(p -> {
                EcoAPI.getBalance(p.getUniqueId());
            });
        });
    }

    /**
     * Stores all online players to the data base
     */
    private void saveOnlinePlayers() {
        ecoManager.getBalanceUsers().forEach(dataBase::updateBalance);
    }

    /**
     * This is an interval that updates
     * the balances of the online users
     * to the data base
     */
    private void updateOnlinePlayers() {
        Async.get().interval(36000).run(this::saveOnlinePlayers);
    }

    /**
     * Updates top 10 players on interval
     */
    private void updateTopTenPlayers() {
        Async.get().interval(18000).run(() -> {
            dataBase.updateTopTenPlayers();
        });
    }
}
