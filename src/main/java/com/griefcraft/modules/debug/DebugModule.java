package com.griefcraft.modules.debug;

import com.griefcraft.lwc.LWC;
import com.griefcraft.lwc.LWCInfo;
import com.griefcraft.model.Protection;
import com.griefcraft.model.Protection.Flag;
import com.griefcraft.scripting.JavaModule;
import com.griefcraft.scripting.event.LWCCommandEvent;
import com.griefcraft.util.Colors;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DebugModule extends JavaModule {

    /**
     * The value for yes in the locale
     */
    private String yes = null;

    /**
     * The value for no in the locale
     */
    private String no = null;

    @Override
    public void load(LWC lwc) {
        yes = lwc.getLocale("yes");
        no = lwc.getLocale("no");
    }

    @Override
    public void onCommand(LWCCommandEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!event.hasFlag("debug")) {
            return;
        }

        LWC lwc = event.getLWC();
        CommandSender sender = event.getSender();
        String[] args = event.getArgs();
        event.setCancelled(true);

        if (!(sender instanceof Player)) {
            sender.sendMessage("This command is only usable by real players :-)");
            return;
        }

        Player player = (Player) sender;

        player.sendMessage(" ");
        player.sendMessage(Colors.Gray + "LWC: " + LWCInfo.FULL_VERSION);
        player.sendMessage(" ");
        player.sendMessage(Colors.Green + "Standard LWC permissions");
        doPermission(player, "lwc.protect");

        doPlayerPermission(player, "lwc.create.public");
        doPlayerPermission(player, "lwc.create.password");
        doPlayerPermission(player, "lwc.create.private");
        doPlayerPermission(player, "lwc.info");
        doPlayerPermission(player, "lwc.remove");
        doPlayerPermission(player, "lwc.modify");
        doPlayerPermission(player, "lwc.unlock");

        for (Flag flag : Protection.Flag.values()) {
            doPlayerPermission(player, "lwc.flag." + flag.toString().toLowerCase());
        }

        player.sendMessage(" ");
        player.sendMessage(Colors.Yellow + "Mod permissions");
        doPermission(player, "lwc.mod");

        player.sendMessage(" ");
        player.sendMessage(Colors.Red + "Admin permissions");
        doPermission(player, "lwc.admin");


        return;
    }

    /**
     * @param player
     * @param node
     */
    private void doPermission(Player player, String node) {
        player.sendMessage(node + ": " + strval(LWC.getInstance().hasPermission(player, node)));
    }

    /**
     * @param player
     * @param node
     */
    private void doPlayerPermission(Player player, String node) {
        player.sendMessage(node + ": " + strval(LWC.getInstance().hasPlayerPermission(player, node)));
    }

    /**
     * @param player
     * @param node
     */
    private void doAdminPermission(Player player, String node) {
        player.sendMessage(node + ": " + strval(LWC.getInstance().hasAdminPermission(player, node)));
    }

    /**
     * Convert a boolean to its yes/no equivilent
     *
     * @param bool
     * @return
     */
    private String strval(boolean bool) {
        return bool ? yes : no;
    }

}
