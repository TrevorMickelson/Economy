package com.mcaim.economy.commands;

import com.mcaim.core.models.ItemBuild;
import com.mcaim.core.models.Permission;
import com.mcaim.core.util.C;
import com.mcaim.core.util.Command;
import com.mcaim.core.util.PlayerUtil;
import com.mcaim.core.util.Util;
import com.mcaim.economy.util.EcoAPI;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class WithdrawCmd extends Command {
    public WithdrawCmd() {
        super(false, Permission.NONE, "withdraw");
        setErrorDisplay("<command> <amount>");
        registerSubCommand(0, "<amount>", "all");
        setPlayerTabCompletion(-1);
    }

    @Override
    protected void onExecute(CommandSender sender, String alias, String[] args) {
        Player player = (Player) sender;
        double amount = args[0].equalsIgnoreCase("all") ? EcoAPI.getBalance(player.getUniqueId()) : Double.parseDouble(args[0]);

        if (amount <= 0) {
            C.msg(player, C.FAIL + "Amount must be greater than 0!");
            return;
        }

        UUID uuid = player.getUniqueId();

        if (EcoAPI.getBalance(uuid) < amount) {
            C.msg(player, C.FAIL + "You don't have that much money!");
            return;
        }

        ItemStack bankNote = ItemBuild.of(Material.PAPER).name("&6&lBanknote &e$" + amount)
        .glow().lore("&7&oRight-Click to obtain money").giveUniqueKey("Banknote").getItem();
        PlayerUtil.giveItem(uuid, bankNote);
        EcoAPI.removeBalance(uuid, amount);

        C.msg(player, C.INFORM + "&e$" + Util.formatNumber(amount) + " &7has been withdrawn from your account");
    }
}
