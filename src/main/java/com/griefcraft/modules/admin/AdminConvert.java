package com.griefcraft.modules.admin;

import com.griefcraft.converters.ChastityChest;
import com.griefcraft.converters.ChestProtect;
import com.griefcraft.lwc.LWC;
import com.griefcraft.scripting.JavaModule;
import com.griefcraft.scripting.event.LWCCommandEvent;
import org.bukkit.command.CommandSender;

public class AdminConvert extends JavaModule {

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

        if (!args[0].equals("convert")) {
            return;
        }

        // we have the right command
        event.setCancelled(true);

        if (args.length < 2) {
            lwc.sendSimpleUsage(sender, "/lwc admin convert <chestprotect|chastity>");
            return;
        }

        String pluginToConvert = args[1].toLowerCase();

        if (pluginToConvert.equals("chestprotect")) {
            new ChestProtect(sender);
        } else if (pluginToConvert.equals("chastity")) {
            new ChastityChest(sender);
        }

        return;
    }

}