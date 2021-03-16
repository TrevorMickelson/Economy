package com.mcaim.economy.commands;

import com.mcaim.core.models.Permission;
import com.mcaim.core.scheduler.Async;
import com.mcaim.core.util.C;
import com.mcaim.core.util.Command;
import com.mcaim.economy.EcoMain;
import org.bukkit.command.CommandSender;

/**
 * Forcefully updates the top 10 balances
 * and force stores everyone to the database
 */
public class ForceUpdateCmd extends Command {
    private final EcoMain plugin = EcoMain.getInstance();

    public ForceUpdateCmd() {
        super(true, Permission.OP, "forceupdatebalances", "fub");
    }

    @Override
    protected void onExecute(CommandSender sender, String alias, String[] args) {
        Async.get().run(() -> {
            plugin.getDataBase().updateTopTenPlayers();
            plugin.getEcoManager().getBalanceUsers().forEach(plugin.getDataBase()::updateBalance);
        });

        C.msg(sender, C.SUCCESS + "Top 10 balances + all online player balances have been forcefully updated!");
    }
}
