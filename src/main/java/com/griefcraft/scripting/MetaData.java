package com.griefcraft.scripting;

public class MetaData {

    /**
     * The module object
     */
    private Module module;
    private boolean loaded = false;

    public MetaData(Module module) {
        this.module = module;
    }

    public Module getModule() {
        return module;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void trigger() {
        loaded = true;
    }

}
