package com;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import com.data_model.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {
    private final RestRepository repository;
    Controller(RestRepository repository) {
        this.repository = repository;
    }
    //Map user operation ---------------------------------------------------------------------------------------
    @GetMapping("/api/user")
    List<App_user> listAllUser() {
        return repository.getAllUsers();
    }

    @GetMapping("/api/user/{unitId}/{time}")
    List<App_user> AllUserByUnit_time(@PathVariable int unitId, @PathVariable String time) throws ParseException {
        return repository.listAllUserByUnit_time(unitId, time);
    }

    @PostMapping("/api/user")
    String CreateOrUpdateUser(@RequestBody App_user newUser) {
        if (newUser.getId() == 0){
            return repository.CreateUser(newUser);
        }
        else {
            return repository.UpdateUser(newUser);
        }
    }

    @DeleteMapping("/api/user/{id}/{version}")
    String deleteUser(@PathVariable int id, @PathVariable int version) {
        return repository.DeleteUser(id, version);
    }

    //Map unit and role operation ---------------------------------------------------------------------------------------
    @GetMapping("/api/unit")
    List<App_unit> AllUnit() {
        return repository.listAllUnits();
    }

    @GetMapping("/api/role")
    List<App_role> AllRole() {
        return repository.listAllRoles();
    }

    //Map userRole operation ---------------------------------------------------------------------------------------
    @GetMapping("/api/userrole/{userid}/{unitid}")
    List<App_userRole> UserRoleBy_user_unit(@PathVariable int userid, @PathVariable int unitid) {
        return repository.listUserRoleBy_user_unit(userid, unitid);
    }

    @GetMapping("/api/userrole/{userid}/{unitid}/{time}")
    List<App_userRole> ValidUserRoles(@PathVariable int userid, @PathVariable int unitid, @PathVariable Timestamp time) throws ParseException {
        return repository.listValidUserRoles(userid, unitid, time);
    }

    @GetMapping(value = "/api/userrole/{unitid}")
    List<App_user_userRole> AllUserRoleByUnit(@PathVariable int unitid) {
        return repository.getAllUserByUnitId(unitid);
    }

    @PostMapping("/api/userrole")
    String CreateOrUpdateUserRole(@RequestBody App_userRole newUserRole) {
        System.out.print("newUserRole:"+ newUserRole.getId() + "  " + newUserRole.getVersion() +"   "+ newUserRole.getUserId()+"   "+newUserRole.getUnitId());
        //System.out.print("oldUser");
        if (newUserRole.getId() == 0){
            return repository.CreateUserRole(newUserRole);
        }
        else {
            return repository.UpdateUserRole(newUserRole);
        }
    }

    @DeleteMapping("/api/userrole/{id}/{version}/{userId}/{unitId}")
    String deleteUserRole(@PathVariable int id, @PathVariable int version, @PathVariable int userId, @PathVariable int unitId) {
        return repository.DeleteUserRole(id, version,userId, unitId);
    }


}
