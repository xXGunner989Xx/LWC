package com.griefcraft.modules.lists;

import com.griefcraft.lwc.LWC;
import com.griefcraft.model.AccessRight;
import com.griefcraft.model.Protection;
import com.griefcraft.model.ProtectionTypes;
import com.griefcraft.scripting.JavaModule;
import com.herocraftonline.dthielke.lists.Lists;
import com.herocraftonline.dthielke.lists.PrivilegedList;
import com.herocraftonline.dthielke.lists.PrivilegedList.PrivilegeLevel;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ListsModule extends JavaModule {

    /**
     * The com.griefcraft.modules.lists api
     */
    private Lists lists = null;

    @Override
    public void load(LWC lwc) {
        Plugin listsPlugin = lwc.getPlugin().getServer().getPluginManager().getPlugin("Lists");

        if (listsPlugin != null) {
            lists = (Lists) listsPlugin;
        }
    }

    @Override
    public Result canAccessProtection(LWC lwc, Player player, Protection protection) {
        if (protection.getType() != ProtectionTypes.PRIVATE) {
            return DEFAULT;
        }

        if (lists != null) {
            for (AccessRight right : protection.getAccessRights()) {
                if (right.getType() != AccessRight.LIST) {
                    continue;
                }

                String listName = right.getName();

                // load the list
                PrivilegedList privilegedList = lists.getList(listName);

                if (privilegedList != null) {
                    PrivilegeLevel privilegeLevel = privilegedList.get(player.getName());

                    // they have access in some way or another, let's allow them in
                    if (privilegeLevel != null) {
                        return ALLOW;
                    }
                }
            }
        }

        return DEFAULT;
    }

}
