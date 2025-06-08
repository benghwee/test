package com.himanshu.kafkabinder;


public class Person {
    private String name;

    private String phone;

    private int block;

    public Person(String name, String phone, int block) {
        this.name = name;
        this.phone = phone;
        this.block = block;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = block;
    }
}
