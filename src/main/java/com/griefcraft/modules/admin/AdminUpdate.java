package com.griefcraft.modules.admin;

import com.griefcraft.lwc.LWC;
import com.griefcraft.scripting.JavaModule;
import com.griefcraft.scripting.event.LWCCommandEvent;
import com.griefcraft.util.Updater;
import org.bukkit.command.CommandSender;

public class AdminUpdate extends JavaModule {

    @Override
    public void onCommand(LWCCommandEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!event.hasFlag("a", "admin")) {
            return;
        }

        LWC lwc = event.getLWC();
        CommandSender sender = event.getSender();
        String[] args = event.getArgs();

        if (!args[0].equals("update")) {
            return;
        }

        // we have the right command
        event.setCancelled(true);

        Updater updater = lwc.getPlugin().getUpdater();
        updater.loadVersions(false);

        if (updater.checkDist()) {
            lwc.sendLocale(sender, "protection.admin.update.updated", "version", updater.getLatestPluginVersion());
        } else {
            lwc.sendLocale(sender, "protection.admin.update.noupdate");
        }

        return;
    }

}