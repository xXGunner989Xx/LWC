package com.griefcraft.model;

public class Limit {

    private int amount;
    private String entity;
    private int id;

    private int type;

    public final static int GLOBAL = 2;

    public final static int GROUP = 0;

    public final static int PLAYER = 1;

    public int getAmount() {
        return amount;
    }

    public String getEntity() {
        return entity;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(int type) {
        this.type = type;
    }

}
