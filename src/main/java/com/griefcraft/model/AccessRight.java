package com.griefcraft.model;

public class AccessRight {

    /**
     * The player has no access to the protection
     */
    public static final int RIGHT_NOACCESS = -1;

    /**
     * The player has player rights to the protection
     */
    public static final int RIGHT_PLAYER = 0;

    /**
     * The player has admin rights to the protection
     */
    public static final int RIGHT_ADMIN = 1;

    /**
     * Access right is for a group
     */
    public static final int GROUP = 0;

    /**
     * Access right is for a player
     */
    public static final int PLAYER = 1;

    /**
     * Used in conjuction with HeroList
     */
    public static final int LIST = 2;

    /**
     * Not saved to the database
     */
    public static final int TEMPORARY = 3;

    /**
     * Used in conjunction with /lwc -O
     */
    public static final int RESULTS_PER_PAGE = 15;

    private String name;
    private int id;

    private int protectionId;

    private int rights;
    private int type;

    @Override
    public String toString() {
        return String.format("AccessRight = %d { protection=%d name=%s rights=%d type=%s }", id, protectionId, name, rights, typeToString(rights));
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getProtectionId() {
        return protectionId;
    }

    public int getRights() {
        return rights;
    }

    public int getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProtectionId(int protectionId) {
        this.protectionId = protectionId;
    }

    public void setRights(int rights) {
        this.rights = rights;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static String typeToString(int type) {
        if (type == GROUP) {
            return "Group";
        } else if (type == PLAYER) {
            return "Player";
        } else if (type == LIST) {
            return "List";
        } else if (type == TEMPORARY) {
            return "Temporary";
        }

        return "Unknown";
    }

}
