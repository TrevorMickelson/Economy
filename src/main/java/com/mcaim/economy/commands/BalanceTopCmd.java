package com.mcaim.economy.commands;

import com.mcaim.core.models.Permission;
import com.mcaim.core.util.C;
import com.mcaim.core.util.Command;
import com.mcaim.core.util.PlayerUtil;
import com.mcaim.core.util.Util;
import com.mcaim.economy.EcoMain;
import org.bukkit.command.CommandSender;

import java.util.Map;
import java.util.UUID;

public class BalanceTopCmd extends Command {
    private final EcoMain plugin = EcoMain.getInstance();

    public BalanceTopCmd() {
        super(true, Permission.NONE, "baltop", "balancetop");
    }

    @Override
    protected void onExecute(CommandSender sender, String alias, String[] args) {
        Map<UUID, Double> topTenUsers = plugin.getEcoManager().getTopTenUsers();

        if (topTenUsers.isEmpty()) {
            C.msg(sender, "&c&lNo one has money yet :(");
            return;
        }

        C.msg(sender, "&6&lTop 10 Balances");

        int index = 1;
        for (Map.Entry<UUID, Double> entry : plugin.getEcoManager().getTopTenUsers().entrySet()) {
            if (index >= 11)
                break;

            String name = PlayerUtil.getName(entry.getKey());
            String balance = Util.formatNumber(entry.getValue());
            C.msg(sender, "&7" + index + ". &f" + name + ": &e" + balance);
            index++;
        }
    }
}
