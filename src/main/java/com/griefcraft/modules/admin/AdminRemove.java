package com.griefcraft.modules.admin;

import com.griefcraft.lwc.LWC;
import com.griefcraft.model.Protection;
import com.griefcraft.scripting.JavaModule;
import com.griefcraft.scripting.event.LWCCommandEvent;
import org.bukkit.command.CommandSender;

public class AdminRemove extends JavaModule {

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

        if (!args[0].equals("remove")) {
            return;
        }

        // we have the right command
        event.setCancelled(true);

        if (args.length < 2) {
            lwc.sendSimpleUsage(sender, "/lwc admin remove <id>");
            return;
        }

        int protectionId;

        try {
            protectionId = Integer.parseInt(args[1]);
        } catch (Exception e) {
            lwc.sendLocale(sender, "protection.admin.remove.invalidid");
            return;
        }

        Protection protection = lwc.getPhysicalDatabase().loadProtection(protectionId);

        if (protection != null) {
            protection.remove();
        }

        lwc.sendLocale(sender, "protection.admin.remove.finalize");


        return;
    }

}