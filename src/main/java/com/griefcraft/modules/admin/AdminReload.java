package com.griefcraft.modules.admin;

import com.griefcraft.lwc.LWC;
import com.griefcraft.scripting.JavaModule;
import com.griefcraft.scripting.event.LWCCommandEvent;
import com.griefcraft.util.config.Configuration;
import org.bukkit.command.CommandSender;

public class AdminReload extends JavaModule {

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

        if (!args[0].equals("reload")) {
            return;
        }

        // we have the right command
        event.setCancelled(true);

        Configuration.reload();

        lwc.sendLocale(sender, "protection.admin.reload.finalize");
        return;
    }

}