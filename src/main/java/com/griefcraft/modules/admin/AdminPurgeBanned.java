package com.griefcraft.modules.admin;

import com.griefcraft.lwc.LWC;
import com.griefcraft.scripting.JavaModule;
import com.griefcraft.scripting.event.LWCCommandEvent;
import org.bukkit.command.CommandSender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AdminPurgeBanned extends JavaModule {

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

        if (!args[0].equals("purgebanned")) {
            return;
        }

        // we have the right command
        event.setCancelled(true);

        boolean shouldRemoveBlocks = args.length > 1 ? args[1].endsWith("remove") : false;
        List<String> players = loadBannedPlayers();

        for (String toRemove : players) {
            lwc.fastRemoveProtections(sender, "owner = '" + toRemove + "'", shouldRemoveBlocks);

            lwc.sendLocale(sender, "protection.admin.purge.finalize", "player", toRemove);
        }
    }

    /**
     * Load the list of currently banned players
     *
     * @return
     */
    private List<String> loadBannedPlayers() {
        List<String> banned = new ArrayList<String>();

        File file = new File("banned-players.txt");

        if (!file.exists()) {
            return banned;
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;

            while ((line = reader.readLine()) != null) {
                banned.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return banned;
    }

}