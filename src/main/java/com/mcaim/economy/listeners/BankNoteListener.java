package com.mcaim.economy.listeners;

import com.mcaim.core.models.ItemBuild;
import com.mcaim.core.models.cooldown.Cooldown;
import com.mcaim.core.util.C;
import com.mcaim.core.util.Util;
import com.mcaim.economy.util.EcoAPI;
import com.mcaim.economy.util.EconParticles;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BankNoteListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Action action = event.getAction();

        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK)
            return;

        ItemStack item = event.getItem();

        if (item == null)
            return;

        // If it's a bank note
        if (ItemBuild.hasUniqueKey(item, "Banknote")) {
            ItemMeta meta = item.getItemMeta();

            if (meta == null)
                return;

            Player player = event.getPlayer();

            // Cooldown
            Cooldown.get(player, "Banknote").color("&6").time(3).run(() -> {
                String name = meta.getDisplayName();
                double getBankBalance = Double.parseDouble(name.substring(name.lastIndexOf("$") + 1));

                item.setAmount(item.getAmount() - 1);
                EcoAPI.addBalance(player.getUniqueId(), getBankBalance);

                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                EconParticles.spawnBankNoteParticle(player);
                C.msg(player, C.INFORM + "&e$" + Util.formatNumber(getBankBalance) + " &7has been added to your account");
            });
        }
    }
}
