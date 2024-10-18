package com.griefcraft.listeners;

import com.griefcraft.lwc.LWCPlugin;
import com.griefcraft.model.Protection;
import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;

public class LWCEntityListener extends EntityListener {

    /**
     * The plugin instance
     */
    private LWCPlugin plugin;

    /**
     * Blast radius for TNT / Creepers
     */
    public final static int BLAST_RADIUS = 4;

    public LWCEntityListener(LWCPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.isCancelled()) {
            return;
        }

        boolean ignoreExplosions = plugin.getLWC().getConfiguration().getBoolean("core.ignoreExplosions", false);

        for (Block block : event.blockList()) {
            Protection protection = plugin.getLWC().getPhysicalDatabase().loadProtection(block.getWorld().getName(), block.getX(), block.getY(), block.getZ());

            if (protection != null) {
                if (ignoreExplosions) {
                    protection.remove();
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }

}
