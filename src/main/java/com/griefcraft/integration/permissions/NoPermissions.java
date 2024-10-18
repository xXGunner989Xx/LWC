package com.griefcraft.integration.permissions;

import com.griefcraft.integration.IPermissions;
import org.bukkit.entity.Player;

import java.util.List;

public class NoPermissions implements IPermissions {

    public boolean isActive() {
        return false;
    }

    public boolean permission(Player player, String node) {
        throw new UnsupportedOperationException("No active permissions system");
    }

    public List<String> getGroups(Player player) {
        throw new UnsupportedOperationException("No active permissions system");
    }

}
