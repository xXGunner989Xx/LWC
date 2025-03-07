package com.griefcraft.util;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


// Possible race condition issues here, but we are in the span of microseconds
public class MinecartEventProcessor {
    public static Queue<VehicleCreateEvent> vehicleCreateEventQueue = new ConcurrentLinkedQueue<>();
    public static Queue<PlayerInteractEvent> playerInteractEventQueue = new ConcurrentLinkedQueue<>();;


    // TODO: this needs to be run in a loop
    public static Pair<PlayerInteractEvent, VehicleCreateEvent> getMinecartPlacedEvents() {
        if (vehicleCreateEventQueue.peek() == null || playerInteractEventQueue.peek() == null) {
            return null;
        }
        return Pair.of(playerInteractEventQueue.peek(), vehicleCreateEventQueue.peek());
    }
}
