package com.griefcraft.listeners;

import com.griefcraft.lwc.LWC;
import com.griefcraft.lwc.LWCPlugin;
import com.griefcraft.util.MinecartEventProcessor;
import org.bukkit.Material;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleListener;

public class LWCVehicleListener extends VehicleListener {
    private LWCPlugin plugin;
    private MinecartEventProcessor minecartEventProcessor;
    public LWCVehicleListener(LWCPlugin plugin) {
        this.plugin = plugin;
        this.minecartEventProcessor = MinecartEventProcessor.getInstance();
    }

    @Override
    public void onVehicleCreate(VehicleCreateEvent event) {
        if (!(event.getVehicle() instanceof StorageMinecart)) {
            return;
        }

        if (!LWC.ENABLED) {
            return;
        }

        LWC lwc = plugin.getLWC();

        if (!lwc.isProtectable(Material.STORAGE_MINECART)) {
            return;
        }

        String autoRegisterType = plugin.getLWC().resolveProtectionConfiguration(Material.STORAGE_MINECART, "autoRegister");

        // is it auto registerable?
        if (!autoRegisterType.equalsIgnoreCase("private") && !autoRegisterType.equalsIgnoreCase("public")) {
            return;
        }

        minecartEventProcessor.addToEventQueue(event);
    }
}
