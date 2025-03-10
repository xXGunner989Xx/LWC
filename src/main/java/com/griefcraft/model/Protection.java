package com.griefcraft.model;

import com.griefcraft.cache.CacheSet;
import com.griefcraft.cache.LRUCache;
import com.griefcraft.lwc.LWC;
import com.griefcraft.scripting.ModuleLoader;
import com.griefcraft.scripting.event.LWCProtectionRemovePostEvent;
import com.griefcraft.util.Colors;
import com.griefcraft.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class Protection {

    // re-use LWC logger
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger("LWC");

    // just a footnote, if a flag is "on", it is SET in the database, however if it's set to "off"
    // it is REMOVED from the database if it is in it!
    public enum Flag {
        /**
         * If set, redstone use is DISABLED if protections.denyRedstone = FALSE
         */
        REDSTONE(0x02),

        /**
         * Attracts dropped items into the inventory
         */
        MAGNET(0x03),

        /**
         * Protection is exempt from -remove; e.g /lwc admin expire -remove 2 weeks
         */
        EXEMPTION(0x08, true);

        Flag(int bit) {
            this(bit, false);
        }

        Flag(int bit, boolean restricted) {
            this.bit = bit;
            this.restricted = restricted;
        }

        private int bit;

        /**
         * If the flag is restricted to lwc admins
         */
        private boolean restricted;

        public int getBit() {
            return bit;
        }

        public boolean isRestricted() {
            return restricted;
        }
    }

    /**
     * List of the accessRightCache rights for the protection
     */
    private final List<AccessRight> accessRightCache = new ArrayList<AccessRight>();

    /**
     * All of the history items associated with this protection
     */
    private final Set<History> historyCache = new HashSet<History>();

    /**
     * The block id
     */
    private int blockId;

    /**
     * The password for the chest
     */
    private String data;

    /**
     * The date created
     */
    private String date;

    /**
     * Unique id (in sql)
     */
    private int id;

    /**
     * Bit-packed flags
     */
    private int flags;

    /**
     * The owner of the chest
     */
    private String owner;

    /**
     * The chest type
     */
    private int type;

    /**
     * The world this protection is in
     */
    private String world;

    /**
     * The x coordinate
     */
    private int x;

    /**
     * The y coordinate
     */
    private int y;

    /**
     * The z coordinate
     */
    private int z;

    /**
     * The timestamp of when the protection was last accessed
     */
    private long lastAccessed;

    /**
     * Immutable flag for the protection. When removed, this bool is switched to true and any setters
     * will no longer work. However, everything is still intact and in memory at this point (for now.)
     */
    private boolean removed = false;

    /**
     * True when the protection has been modified and should be saved
     */
    private boolean modified = false;

    /**
     * Ensure a history object is located in our cache
     * 
     * @param history
     */
    public void checkHistory(History history) {
        if(!historyCache.contains(history)) {
            historyCache.add(history);
        }
    }

    /**
     * Check if a player is the owner of the protection
     *
     * @param player
     * @return
     */
    public boolean isOwner(Player player) {
        LWC lwc = LWC.getInstance();

        return player != null && (owner.equals(player.getName()) || lwc.isAdmin(player));
    }

    /**
     * Create a History object that is attached to this protection
     *
     * @return
     */
    public History createHistoryObject() {
        History history = new History();

        history.setProtectionId(id);
        history.setStatus(History.Status.INACTIVE);

        // add it to the cache
        historyCache.add(history);

        return history;
    }

    /**
     * @return the related history for this protection, which is immutable
     */
    public Set<History> getRelatedHistory() {
        // cache the database's history if we don't have any yet
        if(historyCache.size() == 0) {
            historyCache.addAll(LWC.getInstance().getPhysicalDatabase().loadHistory(this));
        }

        // now we can return an immutable cache
        return Collections.unmodifiableSet(historyCache);
    }

    /**
     * Get the related history for this protection using the given type
     *
     * @param type
     * @return
     */
    public List<History> getRelatedHistory(History.Type type) {
        List<History> matches = new ArrayList<History>();
        Set<History> relatedHistory = getRelatedHistory();

        for (History history : relatedHistory) {
            if (history.getType() == type) {
                matches.add(history);
            }
        }

        return matches;
    }

    /**
     * Check if a flag is toggled
     *
     * @param flag
     * @return
     */
    public boolean hasFlag(Flag flag) {
        return (flags & flag.getBit()) == flag.getBit();
    }

    /**
     * Add a flag to the protection
     *
     * @param flag
     * @return
     */
    public boolean addFlag(Flag flag) {
        if (removed) {
            return false;
        }

        if (!hasFlag(flag)) {
            flags |= flag.getBit();
            modified = true;
            return true;
        }

        return false;
    }

    /**
     * Remove a flag from the protection
     * TODO: redo? :s
     *
     * @param flag
     * @return
     */
    public void removeFlag(Flag flag) {
        if (removed) {
            return;
        }

        this.modified = true;

        if (!hasFlag(flag)) {
            return;
        }

        flags = 0;

        for (Flag tmp : Flag.values()) {
            if (flag != tmp) {
                addFlag(tmp);
            }
        }
    }

    /**
     * Check if the entity + accessRightCache type exists, and if so return the rights (-1 if it does not exist)
     *
     * @param type
     * @param name
     * @return the accessRightCache the player has
     */
    public int getAccess(int type, String name) {
        for (AccessRight right : accessRightCache) {
            if (right.getType() == type && right.getName().equalsIgnoreCase(name)) {
                return right.getRights();
            }
        }

        return -1;
    }

    /**
     * @return the list of accessRightCache rights
     */
    public List<AccessRight> getAccessRights() {
        return accessRightCache;
    }

    /**
     * Remove temporary accessRightCache rights from the protection
     */
    public void removeTemporaryAccessRights() {
        Iterator<AccessRight> iter = accessRightCache.iterator();

        while (iter.hasNext()) {
            AccessRight right = iter.next();

            if (right.getType() == AccessRight.TEMPORARY) {
                iter.remove();
            }
        }
    }

    /**
     * Add an accessRightCache right to the stored list
     *
     * @param right
     */
    public void addAccessRight(AccessRight right) {
        if (removed) {
            return;
        }

        accessRightCache.add(right);
    }

    public int getFlags() {
        return flags;
    }

    public int getBlockId() {
        return blockId;
    }

    public String getData() {
        return data;
    }

    public String getDate() {
        return date;
    }

    public int getId() {
        return id;
    }


    public String getOwner() {
        return owner;
    }

    public int getType() {
        return type;
    }

    public String getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public long getLastAccessed() {
        return lastAccessed;
    }

    public void setBlockId(int blockId) {
        if (removed) {
            return;
        }

        this.blockId = blockId;
        this.modified = true;
    }

    public void setData(String data) {
        if (removed) {
            return;
        }

        this.data = data;
        this.modified = true;
    }

    public void setDate(String date) {
        if (removed) {
            return;
        }

        this.date = date;
        this.modified = true;
    }

    public void setId(int id) {
        if (removed) {
            return;
        }

        this.id = id;
        this.modified = true;
    }

    public void setFlags(int flags) {
        if (removed) {
            return;
        }

        this.flags = flags;
        this.modified = true;
    }

    public void setOwner(String owner) {
        if (removed) {
            return;
        }

        this.owner = owner;
        this.modified = true;
    }

    public void setType(int type) {
        if (removed) {
            return;
        }

        this.type = type;
        this.modified = true;
    }

    public void setWorld(String world) {
        if (removed) {
            return;
        }

        this.world = world;
        this.modified = true;
    }

    public void setX(int x) {
        if (removed) {
            return;
        }

        this.x = x;
        this.modified = true;
    }

    public void setY(int y) {
        if (removed) {
            return;
        }

        this.y = y;
        this.modified = true;
    }

    public void setZ(int z) {
        if (removed) {
            return;
        }

        this.z = z;
        this.modified = true;
    }

    public void setLastAccessed(long lastAccessed) {
        if (removed) {
            return;
        }

        this.lastAccessed = lastAccessed;
        this.modified = true;
    }

    /**
     * Remove the protection from the database
     */
    public void remove() {
        if (removed) {
            return;
        }

        LWC lwc = LWC.getInstance();
        removeTemporaryAccessRights();

        // we're removing it, so assume there are no changes
        modified = false;

        // broadcast the removal event
        // we broadcast before actually removing to give them a chance to use any data that would be removed otherwise
        lwc.getModuleLoader().dispatchEvent(ModuleLoader.Event.POST_REMOVAL, this);
        lwc.getModuleLoader().dispatchEvent(new LWCProtectionRemovePostEvent(this));

        // mark related transactions as inactive
        for (History history : getRelatedHistory(History.Type.TRANSACTION)) {
            if (history.getStatus() != History.Status.ACTIVE) {
                continue;
            }

            history.setStatus(History.Status.INACTIVE);
        }

        // now perform final saving to ensure all history objects are saved immediately
        saveNow();

        // make the protection immutable
        removed = true;

        // and now finally remove it from the database
        lwc.getUpdateThread().unqueueProtectionUpdate(this);
        lwc.getPhysicalDatabase().unregisterProtection(id);
        removeCache();
    }

    /**
     * Remove the protection from cache
     */
    public void removeCache() {
        LWC lwc = LWC.getInstance();
        LRUCache<String, Protection> cache = lwc.getCaches().getProtections();

        cache.remove(getCacheKey());

        /* For Bug 656 workaround we record in-memory any double-chests/etc we find as
        * we find them, since we can't count on Bukkit to reliably return that info later.
        * As a result, when we are removing a protection (and therefore LWC calls this method
        * to remove it's cache object), we need to remove the adjacent block from memory also.
        */
        if (lwc.isBug656WorkAround()) {
            World worldObject = lwc.getPlugin().getServer().getWorld(world);
            List<Block> blocks = lwc.getRelatedBlocks(worldObject, x, y, z);
            for (Block b : blocks) {
                String cacheKey = b.getWorld().getName() + ":" + b.getX() + ":" + b.getY() + ":" + b.getZ();
                cache.remove(cacheKey);
            }
        }
    }

    /**
     * Updates the protection in the protection cache
     * Note that save() and saveNow() call this
     */
    public void update() {
        if (removed) {
            return;
        }

        CacheSet caches = LWC.getInstance().getCaches();
        removeCache();

        Protection temp = LWC.getInstance().getPhysicalDatabase().loadProtection(id);

        if (temp != null) {
            caches.getProtections().put(getCacheKey(), temp);
        }
    }

    /**
     * Queue the protection to be saved
     */
    public void save() {
        if (removed) {
            return;
        }

        LWC.getInstance().getUpdateThread().queueProtectionUpdate(this);
    }

    /**
     * Force a protection update to the live database
     */
    public void saveNow() {
        if (removed) {
            return;
        }

        // only save the protection if it was modified
        if(modified) {
            LWC.getInstance().getPhysicalDatabase().saveProtection(this);
            update();
        }

        // check the cache for history updates
        for(History history : historyCache) {
            // if the history object was modified we need to save it
            if(history.wasModified()) {
                history.saveNow();
            }
        }
    }

    /**
     * @return the key used for the protection cache
     */
    public String getCacheKey() {
        return world + ":" + x + ":" + y + ":" + z;
    }

    /**
     * @return the Bukkit world the protection should be located in
     */
    public World getBukkitWorld() {
        if (world == null || world.isEmpty()) {
            return Bukkit.getServer().getWorlds().get(0);
        }

        return Bukkit.getServer().getWorld(world);
    }

    /**
     * @return the Bukkit Player object of the owner
     */
    public Player getBukkitOwner() {
        return Bukkit.getServer().getPlayer(owner);
    }

    /**
     * @return the block representing the protection in the world
     */
    public Block getBlock() {
        World world = getBukkitWorld();

        if (world == null) {
            return null;
        }

        return world.getBlockAt(x, y, z);
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        // format the flags prettily
        String flags = "";

        for (Flag flag : Flag.values()) {
            if (hasFlag(flag)) {
                flags += flag.toString() + ",";
            }
        }

        if (flags.endsWith(",")) {
            flags = flags.substring(0, flags.length() - 1);
        }

        // format the last accessed time
        String lastAccessed = StringUtils.timeToString((System.currentTimeMillis() / 1000L) - this.lastAccessed);

        if (!lastAccessed.equals("Not yet known")) {
            lastAccessed += " ago";
        }

        return String.format("%s %s" + Colors.White + " " + Colors.Green + "Id=%d Owner=%s Location=[%s %d,%d,%d] Created=%s Flags=%s LastAccessed=%s", typeToString(), (blockId > 0 ? (LWC.materialToString(blockId)) : "Not yet cached"), id, owner, world, x, y, z, date, flags, lastAccessed);
    }

    /**
     * @return string representation of the protection type
     */
    public String typeToString() {
        switch (type) {
            case ProtectionTypes.PRIVATE:
                return "Private";

            case ProtectionTypes.PUBLIC:
                return "Public";

            case ProtectionTypes.PASSWORD:
                return "Password";

            case ProtectionTypes.TRAP_KICK:
                return "Kick trap";

            case ProtectionTypes.TRAP_BAN:
                return "Ban trap";
        }

        return "Unknown(raw:" + type + ")";
    }

}
