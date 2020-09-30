package com.data_model;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class App_user {
    private static AtomicInteger uniqueId = new AtomicInteger();
    private @Id @GeneratedValue int id = 0;
    private int version = 1;
    private String name;

    public App_user() { }

    public App_user(String name) {
        this.id=uniqueId.getAndIncrement()+4;
        this.name = name;
    }

    public App_user(int id, int version, String name) {
        this.id = id;
        this.version = version;
        this.name = name;
    }
    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getVersion() {
        return this.version;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
