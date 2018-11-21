package com.mirowengner.example.spring.model;

/**
 * @author Miroslav Wengner (@miragemiko)
 */
public final class VehicleModel {

    private int id;
    private String name;

    public VehicleModel() {
    }

    public VehicleModel(int id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return "VehicleModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
