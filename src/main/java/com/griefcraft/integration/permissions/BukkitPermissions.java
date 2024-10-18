package com.griefcraft.integration.permissions;

import com.griefcraft.integration.IPermissions;
import com.platymuus.bukkit.permissions.Group;
import com.platymuus.bukkit.permissions.PermissionsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * BukkitPermissions is supported by the CraftBukkit Recommended Build #1000+ ONLY
 */
public class BukkitPermissions implements IPermissions {

    /**
     * The PermissionsBukkit handler
     */
    private PermissionsPlugin handler = null;

    public BukkitPermissions() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("PermissionsBukkit");

        if (plugin == null) {
            return;
        }

        handler = (PermissionsPlugin) plugin;
    }

    public List<String> getGroups(Player player) {
        if (handler == null) {
            return null;
        }

        List<Group> found = handler.getGroups(player.getName());
        List<String> groups = new ArrayList<String>(found.size());

        if (found.size() == 0) {
            return groups;
        }

        // add in the groups
        for (Group group : found) {
            groups.add(group.getName());
        }

        return groups;
    }

}