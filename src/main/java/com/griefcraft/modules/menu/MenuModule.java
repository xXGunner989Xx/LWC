package com.griefcraft.modules.menu;

import com.griefcraft.lwc.LWC;
import com.griefcraft.scripting.JavaModule;
import com.griefcraft.scripting.event.LWCCommandEvent;
import com.griefcraft.util.Colors;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MenuModule extends JavaModule {

    @Override
    public void onCommand(LWCCommandEvent event) {
        if (!event.hasFlag("menu")) {
            return;
        }

        LWC lwc = event.getLWC();
        CommandSender sender = event.getSender();
        String[] args = event.getArgs();

        if (args.length < 1) {
            lwc.sendSimpleUsage(sender, "/lwc menu <basic|advanced>");
            return;
        }

        String newStyle = args[0].toLowerCase();

        if (!newStyle.equals("basic") && !newStyle.equals("advanced")) {
            sender.sendMessage(Colors.Red + "Invalid style.");
            return;
        }

        Player player = (Player) sender;

        lwc.getPhysicalDatabase().setMenuStyle(player.getName(), newStyle);
        lwc.sendLocale(player, "protection.menu.finalize", "style", newStyle);
        return;
    }

}
