package com.griefcraft.listeners;

import com.griefcraft.lwc.LWC;
import com.griefcraft.lwc.LWCPlugin;
import com.griefcraft.model.Protection;
import com.griefcraft.model.ProtectionTypes;
import com.griefcraft.scripting.Module;
import com.griefcraft.scripting.Module.Result;
import com.griefcraft.scripting.ModuleLoader.Event;
import com.griefcraft.scripting.event.*;
import com.griefcraft.util.MinecartEventProcessor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.ContainerBlock;
import org.bukkit.craftbukkit.CraftChunk;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Rails;

import java.util.List;

public class LWCPlayerListener extends PlayerListener {

    /**
     * The plugin instance
     */
    private LWCPlugin plugin;
    private MinecartEventProcessor minecartEventProcessor;

    public LWCPlayerListener(LWCPlugin plugin) {
        this.plugin = plugin;
        this.minecartEventProcessor = MinecartEventProcessor.getInstance();
    }

    @Override
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (!LWC.ENABLED) {
            return;
        }

        Player player = event.getPlayer();
        Item item = event.getItemDrop();
        ItemStack itemStack = item.getItemStack();

        Result result = plugin.getLWC().getModuleLoader().dispatchEvent(Event.DROP_ITEM, player, item, itemStack);
        LWCDropItemEvent evt = new LWCDropItemEvent(player, event);
        plugin.getLWC().getModuleLoader().dispatchEvent(evt);

        if (evt.isCancelled() || result == Result.CANCEL) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onPlayerChat(PlayerChatEvent event) {
        if (!plugin.getLWC().getConfiguration().getBoolean("core.filterunlock", true)) {
            return;
        }

        /**
         * We want to block messages starting with cunlock incase someone screws up /cunlock password.
         */
        String message = event.getMessage();

        if (message.startsWith("cunlock")) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (!LWC.ENABLED) {
            return;
        }

        LWC lwc = plugin.getLWC();
        Player player = event.getPlayer();

        Block clickedBlock = event.getClickedBlock();
        Location location = clickedBlock.getLocation();

        CraftWorld craftWorld = (CraftWorld) clickedBlock.getWorld();
        Block block = new CraftBlock((CraftChunk) craftWorld.getChunkAt(location), location.getBlockX(), location.getBlockY(), location.getBlockZ());
        block.setTypeId(craftWorld.getBlockTypeIdAt(location));

        Material material = block.getType();

        // Hacking around player interact event to track minecart place events
        // Minecarts can only be placed on rails, so check that we are right-clicking a rail block
        // for some reason two events get fired here; no idea why but just going to handle the first event
        if (clickedBlock.getType() != null && clickedBlock.getType().getData() != null &&
                event.getAction() == Action.RIGHT_CLICK_BLOCK &&
                player.getItemInHand().getType().equals(Material.STORAGE_MINECART) &&
                Rails.class.isAssignableFrom(clickedBlock.getType().getData())
        ) {
            if (!lwc.hasPermission(player, "lwc.protect") && lwc.hasPermission(player, "lwc.deny") && !lwc.isAdmin(player) && !lwc.isMod(player)) {
                lwc.sendLocale(player, "protection.interact.error.blocked");
                event.setCancelled(true);
                return;
            }
            material = player.getItemInHand().getType();
            // The minecart must be protectable
            if (!lwc.isProtectable(material)) {
                return;
            }

            minecartEventProcessor.addToEventQueue(event);
        }
        
        // Prevent players with lwc.deny from interacting
        if (block.getState() instanceof ContainerBlock) {
            if (!lwc.hasPermission(player, "lwc.protect") && lwc.hasPermission(player, "lwc.deny") && !lwc.isAdmin(player) && !lwc.isMod(player)) {
                lwc.sendLocale(player, "protection.interact.error.blocked");
                event.setCancelled(true);
                return;
            }
        }

        try {
            List<String> actions = lwc.getMemoryDatabase().getActions(player.getName());
            Protection protection = lwc.findProtection(block);
            Module.Result result = Module.Result.CANCEL;
            boolean canAccess = lwc.canAccessProtection(player, protection);
            boolean canAdmin = lwc.canAdminProtection(player, protection);

            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                boolean ignoreLeftClick = Boolean.parseBoolean(lwc.resolveProtectionConfiguration(material, "ignoreLeftClick"));

                if (ignoreLeftClick) {
                    return;
                }
            } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                boolean ignoreRightClick = Boolean.parseBoolean(lwc.resolveProtectionConfiguration(material, "ignoreRightClick"));

                if (ignoreRightClick) {
                    return;
                }
            }

            if (protection != null) {
                result = lwc.getModuleLoader().dispatchEvent(Event.INTERACT_PROTECTION, player, protection, actions, canAccess, canAdmin);

                if (result == Result.DEFAULT) {
                    LWCProtectionInteractEvent evt = new LWCProtectionInteractEvent(event, protection, actions, canAccess, canAdmin);
                    lwc.getModuleLoader().dispatchEvent(evt);

                    result = evt.getResult();
                }
            } else {
                result = lwc.getModuleLoader().dispatchEvent(Event.INTERACT_BLOCK, player, block, actions);

                if (result == Result.DEFAULT) {
                    LWCBlockInteractEvent evt = new LWCBlockInteractEvent(event, block, actions);
                    lwc.getModuleLoader().dispatchEvent(evt);

                    result = evt.getResult();
                }
            }

            if (result == Module.Result.ALLOW) {
                return;
            }

            if (result == Module.Result.DEFAULT) {
                lwc.enforceAccess(player, protection != null ? protection.getBlock() : block);
            }

            if (!canAccess || result == Module.Result.CANCEL) {
                event.setCancelled(true);
                event.setUseInteractedBlock(org.bukkit.event.Event.Result.DENY);
            }
        } catch (Exception e) {
            event.setCancelled(true);
            event.setUseInteractedBlock(org.bukkit.event.Event.Result.DENY);
            lwc.sendLocale(player, "protection.internalerror", "id", "PLAYER_INTERACT");
            e.printStackTrace();
        }
    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!LWC.ENABLED) {
            return;
        }

        LWC lwc = plugin.getLWC();
        String player = event.getPlayer().getName();

        lwc.getMemoryDatabase().unregisterPlayer(player);
        lwc.getMemoryDatabase().unregisterUnlock(player);
        lwc.getMemoryDatabase().unregisterPendingLock(player);
        lwc.getMemoryDatabase().unregisterAllActions(player);
        lwc.getMemoryDatabase().unregisterAllModes(player);
    }

}
