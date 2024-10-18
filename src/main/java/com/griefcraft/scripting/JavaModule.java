package com.griefcraft.scripting;

import com.griefcraft.lwc.LWC;
import com.griefcraft.model.Protection;
import com.griefcraft.scripting.event.LWCAccessEvent;
import com.griefcraft.scripting.event.LWCBlockInteractEvent;
import com.griefcraft.scripting.event.LWCCommandEvent;
import com.griefcraft.scripting.event.LWCDropItemEvent;
import com.griefcraft.scripting.event.LWCProtectionDestroyEvent;
import com.griefcraft.scripting.event.LWCProtectionInteractEvent;
import com.griefcraft.scripting.event.LWCProtectionRegisterEvent;
import com.griefcraft.scripting.event.LWCProtectionRegistrationPostEvent;
import com.griefcraft.scripting.event.LWCProtectionRemovePostEvent;
import com.griefcraft.scripting.event.LWCRedstoneEvent;
import com.griefcraft.scripting.event.LWCSendLocaleEvent;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class JavaModule implements Module {

    /**
     * Allow the event to occur (e.g allow the redstone, allow a protection destruction, and so on)
     */
    public final static Result ALLOW = Result.ALLOW;

    /**
     * Cancel the event from happening (e.g disallow protection interaction, disallow protection registration)
     */
    public final static Result CANCEL = Result.CANCEL;

    /**
     * The default result returned by events
     */
    public final static Result DEFAULT = Result.DEFAULT;

    public void load(LWC lwc) {
    }

    public void protectionAccessRequest(LWCAccessEvent event) {

    }

    public void onDropItem(LWCDropItemEvent event) {

    }

    public void onCommand(LWCCommandEvent event) {

    }

    public void onRedstone(LWCRedstoneEvent event) {

    }

    public void onDestroyProtection(LWCProtectionDestroyEvent event) {

    }

    public void onProtectionInteract(LWCProtectionInteractEvent event) {

    }

    public void onBlockInteract(LWCBlockInteractEvent event) {

    }

    public void onRegisterProtection(LWCProtectionRegisterEvent event) {

    }

    public void onPostRegistration(LWCProtectionRegistrationPostEvent event) {

    }

    public void onPostRemoval(LWCProtectionRemovePostEvent event) {

    }

    public void onSendLocale(LWCSendLocaleEvent event) {

    }

    @Deprecated
    public Result canAccessProtection(LWC lwc, Player player, Protection protection) {
        return DEFAULT;
    }

    @Deprecated
    public Result canAdminProtection(LWC lwc, Player player, Protection protection) {
        return DEFAULT;
    }

    @Deprecated
    public Result onDropItem(LWC lwc, Player player, Item item, ItemStack itemStack) {
        return DEFAULT;
    }

    @Deprecated
    public Result onCommand(LWC lwc, CommandSender sender, String command, String[] args) {
        return DEFAULT;
    }

    @Deprecated
    public Result onRedstone(LWC lwc, Protection protection, Block block, int current) {
        return DEFAULT;
    }

    @Deprecated
    public Result onDestroyProtection(LWC lwc, Player player, Protection protection, Block block, boolean canAccess, boolean canAdmin) {
        return DEFAULT;
    }

    @Deprecated
    public Result onProtectionInteract(LWC lwc, Player player, Protection protection, List<String> actions, boolean canAccess, boolean canAdmin) {
        return DEFAULT;
    }

    @Deprecated
    public Result onBlockInteract(LWC lwc, Player player, Block block, List<String> actions) {
        return DEFAULT;
    }

    @Deprecated
    public Result onRegisterProtection(LWC lwc, Player player, Block block) {
        return DEFAULT;
    }

    @Deprecated
    public void onPostRegistration(LWC lwc, Protection protection) {

    }

    @Deprecated
    public void onPostRemoval(LWC lwc, Protection protection) {

    }

    @Deprecated
    public Result onSendLocale(LWC lwc, Player player, String locale) {
        return DEFAULT;
    }

}
