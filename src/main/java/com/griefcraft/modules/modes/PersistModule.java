package com.griefcraft.modules.modes;

import com.griefcraft.lwc.LWC;
import com.griefcraft.scripting.JavaModule;
import com.griefcraft.scripting.event.LWCCommandEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PersistModule extends JavaModule {

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

        if (!mode.equals("persist")) {
            return;
        }

        List<String> modes = lwc.getMemoryDatabase().getModes(player.getName());

        if (!modes.contains(mode)) {
            lwc.getMemoryDatabase().registerMode(player.getName(), mode);
            lwc.sendLocale(player, "protection.modes.persist.finalize");
        } else {
            lwc.getMemoryDatabase().unregisterMode(player.getName(), mode);
            lwc.sendLocale(player, "protection.modes.persist.off");
        }

        event.setCancelled(true);
        return;
    }

}
