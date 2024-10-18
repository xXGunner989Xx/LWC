package com.griefcraft.listeners;

import com.griefcraft.lwc.LWC;
import com.griefcraft.lwc.LWCPlugin;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;

public class LWCServerListener extends ServerListener {

    private LWCPlugin plugin;

    public LWCServerListener(LWCPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPluginDisable(PluginDisableEvent event) {
        if (!LWC.ENABLED) {
            return;
        }

        Plugin disabled = event.getPlugin();

        /**
         * Remove any modules registered by the disabled plugin
         */
        plugin.getLWC().getModuleLoader().removeModules(disabled);
    }

}
