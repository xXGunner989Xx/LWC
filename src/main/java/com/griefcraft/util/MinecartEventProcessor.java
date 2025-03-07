package com.griefcraft.util;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


// Possible race condition issues here, but we are in the span of microseconds
public class MinecartEventProcessor {
    private static Queue<VehicleCreateEvent> vehicleCreateEventQueue = new ConcurrentLinkedQueue<>();
    private static Queue<PlayerInteractEvent> playerInteractEventQueue = new ConcurrentLinkedQueue<>();;

    public static boolean addToEventQueue(Event event) {
        if (event instanceof VehicleCreateEvent) {
            vehicleCreateEventQueue.add((VehicleCreateEvent) event);
        } else if (event instanceof PlayerInteractEvent) {
            playerInteractEventQueue.add((PlayerInteractEvent) event);
        } else {
            return false;
        }

        // we have received both events so fire a new protection registration event
        if (playerInteractEventQueue.peek() != null && vehicleCreateEventQueue.peek() != null) {
            // TODO: add the protection registration event logic below

        }
        return true;
    }
}
