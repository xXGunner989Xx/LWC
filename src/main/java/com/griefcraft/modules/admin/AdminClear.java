package com.griefcraft.modules.admin;

import com.griefcraft.lwc.LWC;
import com.griefcraft.scripting.JavaModule;
import com.griefcraft.scripting.event.LWCCommandEvent;
import org.bukkit.command.CommandSender;

public class AdminClear extends JavaModule {

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

        if (!args[0].equals("clear")) {
            return;
        }

        // we have the right command
        event.setCancelled(true);

        if (args.length < 2) {
            lwc.sendSimpleUsage(sender, "/lwc admin clear <protections|rights>");
            return;
        }

        String toClear = args[1].toLowerCase();

        if (toClear.equals("protections")) {
            lwc.getPhysicalDatabase().unregisterProtections();
            lwc.getPhysicalDatabase().unregisterProtectionRights();
        } else if (toClear.equals("rights")) {
            lwc.getPhysicalDatabase().unregisterProtectionRights();
        }

        lwc.sendLocale(sender, "protection.admin.clear." + toClear);
        return;
    }

}