package com.zxc;

public class Request {

    private int id;
    private String name;
    private String lastName;
    private int parentID;


    public Request() {
    }

    public Request(String name, String lastName, int parentID) {
        this.name = name;
        this.lastName = lastName;
        this.parentID = parentID;
    }

    public Request(int id, String name, String lastName, int parentID) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.parentID = parentID;
    }

    public Request(int id, String name, String lastName) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    @Override
    public String toString() {
        return "com.zxc.Request{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", parentID=" + parentID +
                '}';
    }
}
