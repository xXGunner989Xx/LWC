package com.griefcraft.cache;


import com.griefcraft.lwc.LWC;
import com.griefcraft.model.Protection;

import java.util.logging.Logger;

public class CacheSet {

    /**
     * Logging instance
     */
    private Logger logger = Logger.getLogger("Cache");

    /**
     * Caches protections to prevent abusing the database
     */
    private LRUCache<String, Protection> protectionCache;

    public CacheSet() {
        int maxCapacity = LWC.getInstance().getConfiguration().getInt("core.cacheSize", 10000);

        protectionCache = new LRUCache<String, Protection>(maxCapacity);
        logger.info("LWC: Protection cache: 0/" + maxCapacity);
    }

    /**
     * get the cache representing protections
     *
     * @return
     */
    public LRUCache<String, Protection> getProtections() {
        return protectionCache;
    }

}
