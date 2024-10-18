package com.griefcraft.modules.modes;

import com.griefcraft.lwc.LWC;
import com.griefcraft.scripting.JavaModule;
import com.griefcraft.scripting.event.LWCCommandEvent;
import com.griefcraft.scripting.event.LWCSendLocaleEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class NoSpamModule extends JavaModule {

    @Override
    public void onCommand(LWCCommandEvent event) {
        if (!event.hasFlag("p", "mode")) {
            return;
        }

        if(event.isCancelled()) {
            return;
        }

        LWC lwc = event.getLWC();
        CommandSender sender = event.getSender();
        String[] args = event.getArgs();

        Player player = (Player) sender;
        String mode = args[0].toLowerCase();

        if (!mode.equals("nospam")) {
            return;
        }

        List<String> modes = lwc.getMemoryDatabase().getModes(player.getName());

        if (!modes.contains(mode)) {
            lwc.getMemoryDatabase().registerMode(player.getName(), mode);
            lwc.sendLocale(player, "protection.modes.nospam.finalize");
        } else {
            lwc.getMemoryDatabase().unregisterMode(player.getName(), mode);
            lwc.sendLocale(player, "protection.modes.nospam.off");
        }

        event.setCancelled(true);
        return;
    }

    @Override
    public void onSendLocale(LWCSendLocaleEvent event) {
        LWC lwc = event.getLWC();
        Player player = event.getPlayer();
        String locale = event.getLocale();

        List<String> modes = lwc.getMemoryDatabase().getModes(player.getName());

        // they don't intrigue us
        if (!modes.contains("nospam")) {
            return;
        }

        // hide all of the creation messages
        if ((locale.endsWith("create.finalize") && !locale.equals("protection.create.finalize")) || locale.endsWith("notice.protected")) {
            event.setCancelled(true);
        }
    }

}
