package com.himanshu.kafkabinder;

import javax.annotation.Nullable;

public class Packet {
    int cost;
    String item;
    public Packet() {}                        // required to read
    public Packet(int cost, String item){
        this.cost = cost;
        this.item = item;
    }
}
