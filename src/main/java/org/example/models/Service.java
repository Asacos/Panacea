package org.example.models;

public class Service {
    int id;
    String name;
    String cost;

    public Service() {
    }

    public Service(String name, String cost) {
        this.name = name;
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getData(){
        return name + " " + cost;
    }
}
