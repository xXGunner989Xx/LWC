package com.griefcraft.integration.permissions;

import com.griefcraft.integration.IPermissions;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PEXPermissions extends SuperPermsPermissions implements IPermissions {

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public boolean permission(Player player, String node) {
        boolean permission = super.permission(player, node);
        if(permission)
            return true;
        
        return PermissionsEx.getPermissionManager().has(player, node);
    }

    @Override
    public List<String> getGroups(Player player) {
        PermissionUser user = PermissionsEx.getPermissionManager().getUser(player);

        if(user == null) {
            return new ArrayList<String>();
        }

        return Arrays.asList(user.getGroupsNames());
    }

}
