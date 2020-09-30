package com.data_model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.List;

public class App_user_userRole {
    private App_user app_user;
    private List<App_userRole> app_userRole;

    public App_user_userRole(){
    }

    public App_user_userRole(App_user app_user, List<App_userRole> app_userRole ){
        this.app_user = app_user;
        this.app_userRole = app_userRole;
    }

    public App_user getApp_user() { return this.app_user; }
    public List<App_userRole> getApp_userRole() { return this.app_userRole; }

    public void setApp_user(App_user appuser){this.app_user = appuser; }
    public void setApp_userRole(List<App_userRole> userroleList){this.app_userRole = userroleList; }


}
