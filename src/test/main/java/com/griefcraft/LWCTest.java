package com.griefcraft;

import com.griefcraft.bukkit.MockPlayer;
import com.griefcraft.bukkit.MockServer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.junit.Test;

import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static org.junit.Assert.assertEquals;

/**
 * WORLD NAMES UTILIZED
 * - main
 * - nether
 */
public class LWCTest {

    /**
     * mock Bukkit server
     */
    private static MockServer server = null;
    private static Logger logger = Logger.getLogger("TestSuite");

    public LWCTest() {
        if (LWCTest.server == null) {
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new SimpleFormatter() {
                @Override
                public synchronized String format(LogRecord arg0) {
                    return arg0.getMessage();
                }
            });
            Logger global = Logger.getLogger("");
            global.addHandler(consoleHandler);

            LWCTest.server = new MockServer();
        }

        server.softReset();
    }

    @Test
    public void CheckLWC() {
        // assertEquals(getPlugin("LWC").isEnabled(), true);
    }

    @Test
    public void CheckPermissions() {
        // assertEquals(getPlugin("Permissions").isEnabled(), true);
    }

    @Test
    public void CreateHidendra() {
        Player player = new MockPlayer("Hidendra");
        server.addPlayer(player);

        assertEquals(server.getOnlinePlayers().length, 1);
    }

    @Test
    public void Case1() {

    }

    /**
     * @param pluginName The plugin to retrieve
     * @return the plugin object
     */
    private Plugin getPlugin(String pluginName) {
        Plugin plugin = server.getPluginManager().getPlugin(pluginName);

        if (plugin == null) {
            return null;
        }

        return plugin;
    }

}
