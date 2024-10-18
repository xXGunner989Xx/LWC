package com.griefcraft.modules.admin;

import com.griefcraft.lwc.LWC;
import com.griefcraft.scripting.JavaModule;
import com.griefcraft.scripting.event.LWCCommandEvent;
import com.griefcraft.util.Colors;
import org.bukkit.command.CommandSender;

public class AdminCache extends JavaModule {

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

        if (!args[0].equals("cache")) {
            return;
        }

        // we have the right command
        event.setCancelled(true);

        if (args.length > 1) {
            String cmd = args[1].toLowerCase();

            if (cmd.equals("clear")) {
                lwc.getCaches().getProtections().clear();
                sender.sendMessage(Colors.Green + "Caches cleared.");
            }
        }

        int size = lwc.getCaches().getProtections().size();
        int max = lwc.getConfiguration().getInt("core.cacheSize", 10000);

        sender.sendMessage(Colors.Green + size + Colors.Yellow + "/" + Colors.Green + max);
    }

}