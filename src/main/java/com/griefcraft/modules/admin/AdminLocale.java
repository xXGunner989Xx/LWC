package com.griefcraft.modules.admin;

import com.griefcraft.lwc.LWC;
import com.griefcraft.scripting.JavaModule;
import com.griefcraft.scripting.event.LWCCommandEvent;
import com.griefcraft.util.StringUtils;
import org.bukkit.command.CommandSender;

public class AdminLocale extends JavaModule {

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

        if (!args[0].equals("locale")) {
            return;
        }

        // we have the right command
        event.setCancelled(true);

        if (args.length < 2) {
            lwc.sendSimpleUsage(sender, "/lwc admin locale <key> [args]");
            return;
        }

        String locale = args[1];
        String[] localeArgs = new String[0];

        if (args.length > 3) {
            localeArgs = StringUtils.join(args, 3).split(" ");
        }

        if (localeArgs.length > 0) {
            lwc.sendLocale(sender, locale, (Object[]) localeArgs);
        } else {
            lwc.sendLocale(sender, locale);
        }

        return;
    }

}