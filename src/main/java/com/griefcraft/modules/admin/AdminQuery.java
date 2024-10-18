package com.griefcraft.modules.admin;

import com.griefcraft.lwc.LWC;
import com.griefcraft.scripting.JavaModule;
import com.griefcraft.scripting.event.LWCCommandEvent;
import com.griefcraft.util.Colors;
import com.griefcraft.util.StringUtils;
import org.bukkit.command.CommandSender;

import java.sql.Statement;

public class AdminQuery extends JavaModule {

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

        if (!args[0].equals("query")) {
            return;
        }

        // we have the right command
        event.setCancelled(true);

        String query = StringUtils.join(args, 1);

        try {
            Statement statement = lwc.getPhysicalDatabase().getConnection().createStatement();
            statement.executeUpdate(query);
            statement.close();
            sender.sendMessage(Colors.Green + "Done.");
        } catch (Exception e) {
            sender.sendMessage(Colors.Red + "Err: " + e.getMessage());
        }

        return;
    }

}
