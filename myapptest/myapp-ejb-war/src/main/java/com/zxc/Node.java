package com.zxc;

import javax.persistence.*;


@Entity
public class Node {

    @Id
    private int id;

    private String name;

    private String lastName;

    private int parentID;

    public Node() { }



    public Node(int id, String name, String lastName, int parentID) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.parentID = parentID;
    }

    public Node(String name, String lastName, int parentID) {

        this.name = name;
        this.lastName = lastName;
        this.parentID = parentID;
    }

    public int getId() {
        return id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int get_Id() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    @Override
    public String toString() {
        return "{ \"id\": " + id + ", \"name\" :  \"" + name + "\", \"lastName\" :  \"" + lastName + "\", \"parentID\" : " + parentID + " }";
    }
}
