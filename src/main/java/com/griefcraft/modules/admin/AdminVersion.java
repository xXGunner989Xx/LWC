package com.griefcraft.modules.admin;

import com.griefcraft.lwc.LWC;
import com.griefcraft.lwc.LWCInfo;
import com.griefcraft.scripting.JavaModule;
import com.griefcraft.scripting.event.LWCCommandEvent;
import com.griefcraft.util.Colors;
import com.griefcraft.util.Updater;
import org.bukkit.command.CommandSender;

public class AdminVersion extends JavaModule {

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

        if (!args[0].equals("version")) {
            return;
        }

        // we have the right command
        event.setCancelled(true);

        Updater updater = lwc.getPlugin().getUpdater();
        // force a reload of the latest versions
        updater.loadVersions(false);
        String pluginColor = Colors.Green;
        double currPluginVersion = LWCInfo.VERSION;
        double latestPluginVersion = updater.getLatestPluginVersion();

        if (latestPluginVersion > currPluginVersion) {
            pluginColor = Colors.Red;
        }

        String full = LWCInfo.FULL_VERSION;

        lwc.sendLocale(sender, "protection.admin.version.finalize", "plugin_color", pluginColor, "plugin_version", full, "latest_plugin", latestPluginVersion);
        return;
    }

}
