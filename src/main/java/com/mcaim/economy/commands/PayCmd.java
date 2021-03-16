package com.mcaim.economy.commands;

import com.mcaim.core.models.Permission;
import com.mcaim.core.models.cooldown.Cooldown;
import com.mcaim.core.util.C;
import com.mcaim.core.util.Command;
import com.mcaim.core.util.PlayerUtil;
import com.mcaim.core.util.Util;
import com.mcaim.economy.util.EcoAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PayCmd extends Command {
    public PayCmd() {
        super(false, Permission.NONE, "pay");
        registerSubCommand(1, "<amount>");
        setErrorDisplay("<command> <player> <amount>");
    }

    @Override
    protected void onExecute(CommandSender sender, String alias, String[] args) {
        Player player = (Player) sender;
        String name = args[0];
        UUID target = PlayerUtil.getUUID(name);

        if (!PlayerUtil.hasPlayedBefore(target)) {
            C.msg(player, C.FAIL + "That player has never played on this server!");
            return;
        }

        double amount = Double.parseDouble(args[1]);

        if (amount <= 0) {
            C.msg(player, C.FAIL + "Amount must be greater than 0!");
            return;
        }

        UUID uuid = player.getUniqueId();
        double playerBal = EcoAPI.getBalance(uuid);

        if (playerBal < amount) {
            C.msg(player, C.FAIL + "You don't have that much money!");
            return;
        }

        if (uuid.equals(target)) {
            C.msg(player, C.FAIL + "You can't pay yourself!");
            return;
        }

        Cooldown.get(player, "Pay").color("&6").time(5).run(() -> {
            EcoAPI.addBalance(target, amount);
            EcoAPI.removeBalance(uuid, amount);
            C.msg(player, C.INFORM + "You gave " + name + " &e$" + Util.formatNumber(amount));

            Player reciever = Bukkit.getPlayer(target);
            if (reciever != null)
                C.msg(reciever, C.INFORM + "&e$" + Util.formatNumber(amount) + " &7was added to your account");
        });
    }
}
