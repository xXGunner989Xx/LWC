package com.griefcraft.modules.admin;

import com.griefcraft.lwc.LWC;
import com.griefcraft.scripting.JavaModule;
import com.griefcraft.scripting.event.LWCCommandEvent;
import com.griefcraft.util.Colors;
import org.bukkit.command.CommandSender;

public class AdminFlush extends JavaModule {

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

        if (!args[0].equals("flush")) {
            return;
        }

        // we have the right command
        event.setCancelled(true);

        sender.sendMessage(Colors.Green + "Flushing Update Thread..");
        lwc.getUpdateThread().flush();
        sender.sendMessage(Colors.Green + "Done.");
        return;
    }

}