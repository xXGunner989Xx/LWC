package com.griefcraft.modules.modes;

import com.griefcraft.lwc.LWC;
import com.griefcraft.scripting.JavaModule;
import com.griefcraft.scripting.event.LWCCommandEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BaseModeModule extends JavaModule {

    @Override
    public void onCommand(LWCCommandEvent event) {
        if (!event.hasFlag("p", "mode")) {
            return;
        }

        LWC lwc = event.getLWC();
        CommandSender sender = event.getSender();
        String[] args = event.getArgs();

        event.setCancelled(true);

        if (args.length == 0) {
            lwc.sendSimpleUsage(sender, "/lwc mode <mode>");
            return;
        }

        if (!(sender instanceof Player)) {
            return;
        }

        String mode = args[0].toLowerCase();
        Player player = (Player) sender;

        if (!lwc.isModeWhitelisted(player, mode)) {
            if (!lwc.isAdmin(sender) && !lwc.isModeEnabled(mode)) {
                lwc.sendLocale(player, "protection.modes.disabled");
                return;
            }
        }

        event.setCancelled(false);
        return;
    }

}
