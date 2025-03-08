package com.griefcraft.scripting.event;

import com.griefcraft.scripting.ModuleLoader;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.event.Cancellable;

public class LWCProtectionRegisterEvent extends LWCPlayerEvent implements Cancellable {

    private Block block;
    private StorageMinecart minecart;
    private boolean cancelled;

    public LWCProtectionRegisterEvent(Player player, Block block) {
        super(ModuleLoader.Event.REGISTER_PROTECTION, player);

        this.block = block;
        this.minecart = null;
    }

    public LWCProtectionRegisterEvent(Player player, StorageMinecart minecart) {
        super(ModuleLoader.Event.REGISTER_PROTECTION, player);

        this.block = null;
        this.minecart = minecart;
    }

    public Block getBlock() {
        return block;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public StorageMinecart getMinecart() {
        return minecart;
    }

}
