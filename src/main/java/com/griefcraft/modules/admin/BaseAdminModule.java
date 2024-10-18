package com.griefcraft.modules.admin;

import com.griefcraft.lwc.LWC;
import com.griefcraft.scripting.JavaModule;
import com.griefcraft.scripting.event.LWCCommandEvent;
import org.bukkit.command.CommandSender;

public class BaseAdminModule extends JavaModule {

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

        if (args.length == 0) {
            if (lwc.isAdmin(sender)) {
                lwc.sendLocale(sender, "help.admin");
            }

            event.setCancelled(true);
            return;
        } else if (args.length > 0) {
            // check for permissions
            if (!lwc.hasAdminPermission(sender, "lwc.admin." + args[0].toLowerCase())) {
                event.setCancelled(true);
                return;
            }
        }

        return;
    }

}
