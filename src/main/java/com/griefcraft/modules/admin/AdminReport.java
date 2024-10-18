package com.griefcraft.modules.admin;

import com.griefcraft.lwc.LWC;
import com.griefcraft.scripting.JavaModule;
import com.griefcraft.scripting.event.LWCCommandEvent;
import com.griefcraft.util.Colors;
import com.griefcraft.util.Performance;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.command.ColouredConsoleSender;
import org.bukkit.entity.Player;

public class AdminReport extends JavaModule {

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

        if (!args[0].equals("report")) {
            return;
        }

        // we have the right command
        event.setCancelled(true);

        ColouredConsoleSender console = null;
        boolean replaceTabs = false;

        if (sender instanceof Player) {
            console = new ColouredConsoleSender((CraftServer) Bukkit.getServer());
            replaceTabs = true;
        }

        for (String line : Performance.generateReport()) {
            line = Colors.Green + line;

            sender.sendMessage(replaceTabs ? line.replaceAll("\\t", " ") : line);

            if (console != null) {
                console.sendMessage(line);
            }
        }

        return;
    }

}