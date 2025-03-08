package com.griefcraft.util;

import com.griefcraft.lwc.LWC;
import com.griefcraft.lwc.LWCPlugin;
import com.griefcraft.model.Protection;
import com.griefcraft.model.ProtectionTypes;
import com.griefcraft.scripting.Module;
import com.griefcraft.scripting.ModuleLoader;
import com.griefcraft.scripting.event.LWCProtectionRegisterEvent;
import com.griefcraft.scripting.event.LWCProtectionRegistrationPostEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;


// Possible race condition issues here, but we are in the span of microseconds
public class MinecartEventProcessor {
    private static MinecartEventProcessor singleton;

    private LWCPlugin plugin;
    private Logger logger;
    public Queue<VehicleCreateEvent> vehicleCreateEventQueue = new ConcurrentLinkedQueue<>();
    public Queue<PlayerInteractEvent> playerInteractEventQueue = new ConcurrentLinkedQueue<>();;

    private MinecartEventProcessor(LWCPlugin plugin) {
        this.plugin = plugin;
        this.logger = Logger.getLogger("LWC");
    }

    public static void initInstance(LWCPlugin plugin) {
        if (singleton != null) {
            return;
        }
        singleton = new MinecartEventProcessor(plugin);
    }
    public static synchronized MinecartEventProcessor getInstance() {
        return singleton;
    }
    public void addToEventQueue(Event event) {
        if (event instanceof VehicleCreateEvent) {
            vehicleCreateEventQueue.add((VehicleCreateEvent) event);
        } else if (event instanceof PlayerInteractEvent) {
            playerInteractEventQueue.add((PlayerInteractEvent) event);
        }

        if (playerInteractEventQueue.peek() != null && vehicleCreateEventQueue.peek() != null) {
            // we have received both events so fire a new protection registration event
            PlayerInteractEvent playerInteractEvent = playerInteractEventQueue.remove();
            VehicleCreateEvent vehicleCreateEvent = vehicleCreateEventQueue.remove();

            // clear the queues right after we get the current events to minimize risk of race condition
            playerInteractEventQueue.clear();
            vehicleCreateEventQueue.clear();

            registerLWCProtection(playerInteractEvent, vehicleCreateEvent);


        }
    }

    private void registerLWCProtection(PlayerInteractEvent playerInteractEvent, VehicleCreateEvent vehicleCreateEvent) {
        Material material = Material.STORAGE_MINECART;
        Player player = playerInteractEvent.getPlayer();
        LWC lwc = plugin.getLWC();
        StorageMinecart minecart = (StorageMinecart) vehicleCreateEvent.getVehicle();

        String autoRegisterType = plugin.getLWC().resolveProtectionConfiguration(material, "autoRegister");

        // is it auto registerable?
        if (!autoRegisterType.equalsIgnoreCase("private") && !autoRegisterType.equalsIgnoreCase("public")) {
            return;
        }

        if (!lwc.hasPermission(player, "lwc.create." + autoRegisterType, "lwc.create", "lwc.protect")) {
            return;
        }

        // default to public
        int type = ProtectionTypes.PUBLIC;

        if (autoRegisterType.equalsIgnoreCase("private")) {
            type = ProtectionTypes.PRIVATE;
        }

        try {
            Module.Result registerProtection = lwc.getModuleLoader().dispatchEvent(ModuleLoader.Event.REGISTER_PROTECTION, player, minecart);
            LWCProtectionRegisterEvent evt = new LWCProtectionRegisterEvent(player, minecart);
            lwc.getModuleLoader().dispatchEvent(evt);
            // something cancelled registration
            if (evt.isCancelled() || registerProtection == Module.Result.CANCEL) {
                return;
            }
            // All good!
            Protection protection = lwc.getPhysicalDatabase().registerProtection(minecart.getUniqueId(), type, minecart.getWorld().getName(), player.getName(), "", minecart.getLocation().getBlockX(), minecart.getLocation().getBlockY(), minecart.getLocation().getBlockZ());
            lwc.sendLocale(player, "protection.onplace.create.finalize", "type", lwc.getLocale(autoRegisterType.toLowerCase()), "block", LWC.materialToString(Material.STORAGE_MINECART.getId()));

            if (protection != null) {
                lwc.getModuleLoader().dispatchEvent(ModuleLoader.Event.POST_REGISTRATION, protection);
                lwc.getModuleLoader().dispatchEvent(new LWCProtectionRegistrationPostEvent(protection));
            }
        } catch (Exception e) {
            lwc.sendLocale(player, "protection.internalerror", "id", "PLAYER_INTERACT");
            e.printStackTrace();
        }
    }
}
