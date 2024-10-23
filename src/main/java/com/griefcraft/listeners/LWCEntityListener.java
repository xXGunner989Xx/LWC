package com.griefcraft.listeners;

import com.griefcraft.lwc.LWC;
import com.griefcraft.lwc.LWCPlugin;
import com.griefcraft.model.Protection;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;

import java.util.List;

public class LWCEntityListener extends EntityListener {

    /**
     * The plugin instance
     */
    private LWCPlugin plugin;

    /**
     * Blast radius for TNT / Creepers
     */
    public final static int BLAST_RADIUS = 5;

    public LWCEntityListener(LWCPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.isCancelled()) {
            return;
        }

        boolean hasProtection = false;
        for (Block block : event.blockList()) {
            if (LWC.getInstance().isProtectable(block)) {
                hasProtection = true;
                break;
            }
        }

        if (hasProtection) {
            boolean ignoreExplosions = plugin.getLWC().getConfiguration().getBoolean("core.ignoreExplosions", false);

            Entity en = event.getEntity();
            List<Protection> protections = plugin.getLWC().getPhysicalDatabase().loadProtections(
                    en.getWorld().getName(),
                    en.getLocation().getBlockX(),
                    en.getLocation().getBlockY(),
                    en.getLocation().getBlockZ(), BLAST_RADIUS);

            if (!protections.isEmpty()) {
                if (ignoreExplosions) {
                    for (Protection protection : protections) {
                        protection.remove();
                    }
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }

}
