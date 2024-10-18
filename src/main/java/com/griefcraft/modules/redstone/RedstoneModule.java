package com.griefcraft.modules.redstone;

import com.griefcraft.lwc.LWC;
import com.griefcraft.model.Protection;
import com.griefcraft.scripting.JavaModule;
import com.griefcraft.scripting.event.LWCRedstoneEvent;

public class RedstoneModule extends JavaModule {

    @Override
    public void onRedstone(LWCRedstoneEvent event) {
        if (event.isCancelled()) {
            return;
        }

        LWC lwc = event.getLWC();
        Protection protection = event.getProtection();

        boolean hasFlag = protection.hasFlag(Protection.Flag.REDSTONE);
        boolean denyRedstone = lwc.getConfiguration().getBoolean("protections.denyRedstone", false);

        if ((!hasFlag && denyRedstone) || (hasFlag && !denyRedstone)) {
            event.setCancelled(true);
            return;
        }

        return;
    }

}
