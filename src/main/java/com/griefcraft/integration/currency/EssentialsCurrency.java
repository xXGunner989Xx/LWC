package com.griefcraft.integration.currency;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import com.griefcraft.integration.ICurrency;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class EssentialsCurrency implements ICurrency {

    /**
     * The Essentials plugin object
     */
    private Essentials essentials;

    public EssentialsCurrency() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Essentials");

        if(plugin == null) {
            return;
        }

        essentials = (Essentials) plugin;
    }

    public boolean isActive() {
        return false;
    }

    public String format(double money) {
        return Economy.format(money);
    }

    public String getMoneyName() {
        return essentials.getSettings().getCurrencySymbol();
    }

    public double getBalance(Player player) {
        try {
            return Economy.getMoney(player.getName());
        } catch(UserDoesNotExistException e) {
            return 0d;
        }
    }

    public boolean canAfford(Player player, double money) {
        try {
            return Economy.hasEnough(player.getName(), money);
        } catch(UserDoesNotExistException e) {
            return false;
        }
    }

    public double addMoney(Player player, double money) {
        try {
            Economy.add(player.getName(), money);
        } catch(UserDoesNotExistException e) {
            return 0;
        } catch(NoLoanPermittedException e) {
            return 0;
        }

        return getBalance(player);
    }

    public double removeMoney(Player player, double money) {
        try {
            Economy.subtract(player.getName(), money);
        } catch(UserDoesNotExistException e) {
            return 0;
        } catch(NoLoanPermittedException e) {
            return 0;
        }

        return getBalance(player);
    }

}
