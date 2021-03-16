package com.mcaim.economy.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class EcoManager {
    private final Map<UUID, Double> balanceUsers = new HashMap<>();
    private LinkedHashMap<UUID, Double> topTenUsers = new LinkedHashMap<>();

    public void addUser(UUID uuid, double balance) {
        balanceUsers.put(uuid, balance);
    }

    public void removeUser(UUID uuid) {
        balanceUsers.remove(uuid);
    }

    public boolean containsUser(UUID uuid) {
        return balanceUsers.containsKey(uuid);
    }

    public Double getBalance(UUID uuid) {
        return balanceUsers.get(uuid);
    }

    public void updateBalance(UUID uuid, Double newBalance) {
        Double oldBalance = balanceUsers.get(uuid);
        balanceUsers.replace(uuid, oldBalance, newBalance);
    }

    public Map<UUID, Double> getBalanceUsers() { return balanceUsers; }

    public void setTopTenUsers(LinkedHashMap<UUID, Double> topTenUsers) { this.topTenUsers = topTenUsers; }
    public LinkedHashMap<UUID, Double> getTopTenUsers() { return topTenUsers; }
}
