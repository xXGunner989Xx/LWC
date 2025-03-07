package com.griefcraft.util;


import com.griefcraft.lwc.LWC;
import com.griefcraft.model.Protection;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateThread implements Runnable {

    /**
     * Queue that protections can be added to to update them in the database periodically in a seperate thread
     */
    private Map<Integer, Protection> protectionUpdateQueue = Collections.synchronizedMap(new HashMap<Integer, Protection>());

    /**
     * True begins the flush
     */
    private boolean flush = false;

    /**
     * The last update
     */
    private long lastUpdate = -1L;

    private Logger logger = Logger.getLogger("Cache");

    /**
     * The LWC object
     */
    private LWC lwc;

    /**
     * If the update thread is running
     */
    private boolean running = false;

    /**
     * Thread being used
     */
    private Thread thread;

    public UpdateThread(LWC lwc) {
        this.lwc = lwc;
        running = true;
        lastUpdate = System.currentTimeMillis();

        thread = new Thread(this);
        thread.start();
    }

    /**
     * Activate flushing
     */
    public void flush() {
        _flush();
    }

    /**
     * @return the size of the waiting queue
     */
    public int size() {
        return protectionUpdateQueue.size();
    }

    /**
     * Add a protection to be updated to the top of the queue (JUST block ids!!)
     *
     * @param protection
     */
    public void queueProtectionUpdate(Protection protection) {
        protectionUpdateQueue.put(protection.getId(), protection);
    }

    /**
     * Unqueue a protection to be updated to the database if it's already queued
     *
     * @param protection
     */
    public void unqueueProtectionUpdate(Protection protection) {
       if(protectionUpdateQueue.containsKey(protection.getId())) {
           protectionUpdateQueue.remove(protection.getId());
       }
    }

    public void run() {
        while (running) {
            lwc.getPlugin().getServer().broadcastMessage("loop iteration");
            lwc.getPlugin().getServer().broadcastMessage(Integer.toString(MinecartEventProcessor.vehicleCreateEventQueue.size()));
            lwc.getPlugin().getServer().broadcastMessage(Integer.toString(MinecartEventProcessor.playerInteractEventQueue.size()));
            if (flush) {
                _flush();
                continue;
            }

            int flushInterval = lwc.getConfiguration().getInt("core.flushInterval", 5);
            long curr = System.currentTimeMillis();
            long interval = flushInterval * 1000L;

            if (curr - lastUpdate > interval) {
                flush = true;
            }

            try {
                Thread.sleep(1000L);
            } catch (Exception e) {
            }
        }
    }

    /**
     * Stop the update thread. Also flush the remaining updates since we're stopping anyway
     */
    public void stop() {
        running = false;
        _flush();

        if (thread != null && !thread.isInterrupted()) {
            thread.interrupt();
        }
    }

    /**
     * Flush any caches to the database
     */
    private void _flush() {
        // periodically update protections in the database if a non-critical change was made
        if (protectionUpdateQueue.size() > 0) {
            Connection connection = lwc.getPhysicalDatabase().getConnection();

            try {
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // save all of the protections
            for(Map.Entry<Integer, Protection> entry : protectionUpdateQueue.entrySet()) {
                Protection protection = entry.getValue();

                protection.saveNow();
            }

            // clear the queue
            protectionUpdateQueue.clear();

            // commit
            try {
                connection.commit();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        flush = false;
        lastUpdate = System.currentTimeMillis();
    }

}
