package com.asgarov.daodemoapp.domain;

public class StudentGroup {
    private int id;
    private String name;

    public StudentGroup() {
    }

    public StudentGroup(String name) {
        this.name = name;
    }

    public StudentGroup(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

}
