package com.griefcraft.modules.unlock;

import com.griefcraft.lwc.LWC;
import com.griefcraft.model.Protection;
import com.griefcraft.model.ProtectionTypes;
import com.griefcraft.scripting.JavaModule;
import com.griefcraft.scripting.event.LWCCommandEvent;
import com.griefcraft.util.Colors;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.griefcraft.util.StringUtils.encrypt;
import static com.griefcraft.util.StringUtils.join;

public class UnlockModule extends JavaModule {

    @Override
    public void onCommand(LWCCommandEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!event.hasFlag("u", "unlock")) {
            return;
        }

        LWC lwc = event.getLWC();
        CommandSender sender = event.getSender();
        String[] args = event.getArgs();
        event.setCancelled(true);

        if (!(sender instanceof Player)) {
            sender.sendMessage(Colors.Red + "Console is not supported.");
            return;
        }

        if (!lwc.hasPlayerPermission(sender, "lwc.unlock")) {
            lwc.sendLocale(sender, "protection.accessdenied");
            return;
        }

        if (args.length < 1) {
            lwc.sendSimpleUsage(sender, "/lwc -u <Password>");
            return;
        }

        Player player = (Player) sender;
        String password = join(args, 0);
        password = encrypt(password);

        if (!lwc.getMemoryDatabase().hasPendingUnlock(player.getName())) {
            player.sendMessage(Colors.Red + "Nothing selected. Open a locked protection first.");
            return;
        } else {
            int chestID = lwc.getMemoryDatabase().getUnlockID(player.getName());

            if (chestID == -1) {
                lwc.sendLocale(player, "protection.internalerror", "id", "ulock");
                return;
            }

            Protection entity = lwc.getPhysicalDatabase().loadProtection(chestID);

            if (entity.getType() != ProtectionTypes.PASSWORD) {
                lwc.sendLocale(player, "protection.unlock.notpassword");
                return;
            }

            if (entity.getData().equals(password)) {
                lwc.getMemoryDatabase().unregisterUnlock(player.getName());
                lwc.getMemoryDatabase().registerPlayer(player.getName(), chestID);
                lwc.sendLocale(player, "protection.unlock.password.valid");
            } else {
                lwc.sendLocale(player, "protection.unlock.password.invalid");
            }
        }

        return;
    }

}
