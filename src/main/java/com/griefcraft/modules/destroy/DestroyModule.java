package com.griefcraft.modules.destroy;

import com.griefcraft.lwc.LWC;
import com.griefcraft.model.History;
import com.griefcraft.model.Protection;
import com.griefcraft.scripting.JavaModule;
import com.griefcraft.scripting.event.LWCProtectionDestroyEvent;
import org.bukkit.entity.Player;

public class DestroyModule extends JavaModule {

    public void onDestroyProtection(LWCProtectionDestroyEvent event) {
        if (event.isCancelled()) {
            return;
        }

        LWC lwc = event.getLWC();
        Protection protection = event.getProtection();
        Player player = event.getPlayer();

        boolean isOwner = protection.isOwner(player);

        if (isOwner) {
            // bind the player who destroyed the protection
            for(History history : protection.getRelatedHistory(History.Type.TRANSACTION)) {
                if(history.getStatus() != History.Status.ACTIVE) {
                    continue;
                }
                
                history.addMetaData("destroyer=" + player.getName());
                history.sync();
            }

            protection.remove();
            lwc.sendLocale(player, "protection.unregistered", "block", LWC.materialToString(protection.getBlockId()));
            return;
        }

        event.setCancelled(true);
        return;
    }

}
