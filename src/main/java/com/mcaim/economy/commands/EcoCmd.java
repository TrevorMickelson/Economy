package com.mcaim.economy.commands;

import com.mcaim.core.models.Permission;
import com.mcaim.core.util.C;
import com.mcaim.core.util.Command;
import com.mcaim.core.util.PlayerUtil;
import com.mcaim.core.util.Util;
import com.mcaim.economy.util.EcoAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class EcoCmd extends Command {
    public EcoCmd() {
        super(true, Permission.OP, "eco", "economy");
        registerSubCommand(0, "give", "remove", "set");
        registerSubCommand(2, "<amount>");
        setPlayerTabCompletion(1);
        setErrorDisplay("<command> [give/remove/set] <player> <amount>");
    }

    @Override
    protected void onExecute(CommandSender sender, String alias, String[] args) {
        String target = args[1];
        UUID uuid = PlayerUtil.getUUID(target);
        double amount = Double.parseDouble(args[2]);

        if (amount <= 0) {
            C.msg(sender, C.FAIL + "Amount must be greater than 0!");
            return;
        }

        if (!PlayerUtil.hasPlayedBefore(uuid)) {
            C.msg(sender, C.FAIL + "That player has never played on this server!");
            return;
        }

        switch (args[0].toLowerCase()) {
            case "give":
                EcoAPI.addBalance(uuid, amount);
                break;

            case "remove":
                EcoAPI.removeBalance(uuid, amount);
                break;

            case "set":
                EcoAPI.setBalance(uuid, amount);
                break;
        }

        C.msg(sender,C.INFORM + target + "'s balance has been updated to: &e" + EcoAPI.getBalanceFormatted(uuid));

        Player player = Bukkit.getPlayer(uuid);
        if (player != null)
            C.msg(player, C.INFORM + "&e$" + Util.formatNumber(amount) + " &7was added to your account");
    }
}
