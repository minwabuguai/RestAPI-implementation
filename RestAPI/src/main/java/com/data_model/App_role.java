package com.data_model;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
public class App_role {
    private static AtomicInteger uniqueId = new AtomicInteger();
    private @Id @GeneratedValue int id = 0;
    private int version = 1;
    private String name;

    public App_role() { }

    public App_role(int id, int version, String name) {
        this.id=id;
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

    public void setRole(int version) {
        this.version = version;
    }
}
