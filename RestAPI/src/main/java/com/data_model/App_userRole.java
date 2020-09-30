package com.data_model;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
public class App_userRole {
    private static AtomicInteger uniqueId = new AtomicInteger();
    private @Id @GeneratedValue int id = 0;
    private int version=1;
    private int UserId;
    private int UnitId;
    private int RoleId;
    private Timestamp ValidFrom;
    private Timestamp ValidTo;

    public App_userRole() {
    }

    public App_userRole(int UserId,int UnitId,int RoleId,Timestamp ValidFrom, Timestamp ValidTo) {
        this.id=uniqueId.getAndIncrement()+1010;
        this.UserId = UserId;
        this.UnitId = UnitId;
        this.RoleId = RoleId;
        this.ValidFrom = ValidFrom;
        this.ValidTo = ValidTo;
    }

    public App_userRole(int id, int version,int UserId,int UnitId,int RoleId,Timestamp ValidFrom, Timestamp ValidTo) {
        this.id = id;
        this.version = version;
        this.UserId = UserId;
        this.UnitId = UnitId;
        this.RoleId = RoleId;
        this.ValidFrom = ValidFrom;
        this.ValidTo = ValidTo;

    }

    public int getId() {
        return this.id;
    }


    public int getUserId() {
        return this.UserId;
    }

    public int getUnitId() {
        return this.UnitId;
    }

    public int getRoleId() {
        return this.RoleId;
    }

    public Timestamp getValidFrom() {
        return this.ValidFrom;
    }

    public Timestamp getValidTo() {
        return this.ValidTo;
    }

    public int getVersion() {
        return this.version;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setUserId(int UserId) {
        this.UserId = UserId;
    }

    public void setUnitId(int UnitId) {
        this.UnitId = UnitId;
    }
    public void setRoleId(int RoleId) {
        this.RoleId = RoleId;
    }
    public void setValidFrom(Timestamp ValidFrom) {
        this.ValidFrom = ValidFrom;
    }
    public void setValidTo(Timestamp ValidTo) {
        this.ValidTo = ValidTo;
    }

}
