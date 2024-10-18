package com.griefcraft.util;

public class UpdaterFile {

    /**
     * The local url location
     */
    private String localLocation;

    /**
     * The remote url location
     */
    private String remoteLocation;

    public UpdaterFile(String location) {
        remoteLocation = location;
        localLocation = location;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UpdaterFile)) {
            return false;
        }

        UpdaterFile other = (UpdaterFile) obj;

        return other.getLocalLocation().equals(localLocation) && other.getRemoteLocation().equals(remoteLocation);
    }

    /**
     * @return the local file location
     */
    public String getLocalLocation() {
        return localLocation;
    }

    /**
     * @return the remote url location
     */
    public String getRemoteLocation() {
        return remoteLocation;
    }

    /**
     * Set the local file location
     *
     * @param localLocation
     */
    public void setLocalLocation(String localLocation) {
        this.localLocation = localLocation;
    }

    /**
     * Set the remote url location
     *
     * @param remoteLocation
     */
    public void setRemoteLocation(String remoteLocation) {
        this.remoteLocation = remoteLocation;
    }

}
