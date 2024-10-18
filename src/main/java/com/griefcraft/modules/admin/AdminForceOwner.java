package com.griefcraft.modules.admin;

import com.griefcraft.lwc.LWC;
import com.griefcraft.model.Action;
import com.griefcraft.model.Protection;
import com.griefcraft.scripting.JavaModule;
import com.griefcraft.scripting.event.LWCBlockInteractEvent;
import com.griefcraft.scripting.event.LWCCommandEvent;
import com.griefcraft.scripting.event.LWCProtectionInteractEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminForceOwner extends JavaModule {

    @Override
    public void onProtectionInteract(LWCProtectionInteractEvent event) {
        if (event.getResult() != Result.DEFAULT) {
            return;
        }

        if (!event.hasAction("forceowner")) {
            return;
        }

        LWC lwc = event.getLWC();
        Protection protection = event.getProtection();
        Player player = event.getPlayer();

        Action action = lwc.getMemoryDatabase().getAction("forceowner", player.getName());
        String newOwner = action.getData();

        protection.setOwner(newOwner);
        protection.save();

        lwc.sendLocale(player, "protection.interact.forceowner.finalize", "player", newOwner);
        lwc.removeModes(player);
        event.setResult(Result.CANCEL);

        return;
    }

    @Override
    public void onBlockInteract(LWCBlockInteractEvent event) {
        if (event.getResult() != Result.DEFAULT) {
            return;
        }

        if (!event.hasAction("forceowner")) {
            return;
        }

        LWC lwc = event.getLWC();
        Player player = event.getPlayer();

        lwc.sendLocale(player, "protection.interact.error.notregistered", "block", LWC.materialToString(event.getBlock()));
        lwc.removeModes(player);
        event.setResult(Result.CANCEL);
        return;
    }

    @Override
    public void onCommand(LWCCommandEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!event.hasFlag("a", "admin")) {
            return;
        }

        LWC lwc = event.getLWC();
        CommandSender sender = event.getSender();
        String[] args = event.getArgs();

        if (!args[0].equals("forceowner")) {
            return;
        }

        // we have the right command
        event.setCancelled(true);

        if (args.length < 2) {
            lwc.sendSimpleUsage(sender, "/lwc admin forceowner <player>");
            return;
        }

        if (!(sender instanceof Player)) {
            lwc.sendLocale(sender, "protection.admin.noconsole");
            return;
        }

        Player player = (Player) sender;
        String newOwner = args[1];

        lwc.getMemoryDatabase().registerAction("forceowner", player.getName(), newOwner);
        lwc.sendLocale(sender, "protection.admin.forceowner.finalize", "player", newOwner);

        return;
    }

}