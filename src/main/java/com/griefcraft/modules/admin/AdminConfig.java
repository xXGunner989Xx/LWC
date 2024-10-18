package com.griefcraft.modules.admin;

import com.griefcraft.lwc.LWC;
import com.griefcraft.scripting.JavaModule;
import com.griefcraft.scripting.event.LWCCommandEvent;
import org.bukkit.command.CommandSender;

public class AdminConfig extends JavaModule {

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

        if (!args[0].equals("config")) {
            return;
        }

        // we have the right command
        event.setCancelled(true);

        if (args.length < 3) {
            lwc.sendSimpleUsage(sender, "/lwc admin config <path> <value>");
            return;
        }

        String path = args[1];
        String value = args[2];

        lwc.getConfiguration().setProperty(path, value);
        return;
    }

}
