package com.mcaim.economy.listeners;

import com.mcaim.core.scheduler.Async;
import com.mcaim.economy.EcoMain;
import com.mcaim.economy.util.EcoAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class JoinLeaveListener implements Listener {
    private final EcoMain plugin = EcoMain.getInstance();

    @EventHandler (priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        Async.get().run(() -> {
            EcoAPI.getBalance(event.getPlayer().getUniqueId());
        });
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onLeave(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        Async.get().run(() -> {
            plugin.getDataBase().updateBalance(uuid, EcoAPI.getBalance(uuid));
            plugin.getEcoManager().removeUser(uuid);
        });
    }
}
