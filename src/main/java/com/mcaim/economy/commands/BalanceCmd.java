package com.mcaim.economy.commands;

import com.mcaim.core.models.Permission;
import com.mcaim.core.util.C;
import com.mcaim.core.util.Command;
import com.mcaim.core.util.PlayerUtil;
import com.mcaim.economy.util.EcoAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BalanceCmd extends Command {
    public BalanceCmd() {
        super(false, Permission.NONE, "bal", "balance");
    }

    @Override
    protected void onExecute(CommandSender sender, String alias, String[] args) {
        Player player = (Player) sender;

        if (args.length == 0) {
            C.msg(player, C.INFORM + "Your balance: &e$" + EcoAPI.getBalanceFormatted(player.getUniqueId()));
            return;
        }

        UUID targetUUID = PlayerUtil.getUUID(args[0]);

        if (!PlayerUtil.hasPlayedBefore(targetUUID)) {
            C.msg(player, C.FAIL + "That player has never played on this server!");
            return;
        }

        C.msg(player, C.INFORM + args[0] + " has a balance of &e$" + EcoAPI.getBalanceFormatted(targetUUID));
    }
}
