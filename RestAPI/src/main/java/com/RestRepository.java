package com;
import com.data_model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class RestRepository {
    @Autowired
    JdbcTemplate template;

    public List<App_user> getAllUsers() {
        String sql = "SELECT * FROM User_table";
        List<App_user> items = template.query(sql,
                (result, rowNum) -> new App_user(
                        result.getInt("id"),
                        result.getInt("version"),
                        result.getString("name")
                ));
        System.out.print("item:" +items);
        return items;
    }

    public App_user GetUserById(int itemId) {
        String sql = "SELECT * FROM User_table WHERE ID=?";
        return template.queryForObject(sql,
                (result,
                 rowNum) -> new App_user(
                         result.getInt("id"),
                         result.getInt("version"),
                         result.getString("name")),
                itemId);
    }

    public List<App_user> listAllUserByUnit_time(int unitId, String time) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date parsedDate = dateFormat.parse(String.valueOf(time));
        Timestamp timestamp = new Timestamp(parsedDate.getTime());
        String sql = "SELECT DISTINCT User_table.id, User_table.version, User_table.name " +
                "FROM User_table " +
                "INNER JOIN UserRole_table ON User_table.id = UserRole_table.UserId " +
                "AND UserRole_table.UnitId = ? AND ? > UserRole_table.ValidFrom " +
                "AND ? < UserRole_table.ValidTo";

        List<App_user> items = template.query(sql,
                (result, rowNum) -> new App_user(
                        result.getInt("id"),
                        result.getInt("version"),
                        result.getString("name")),
                unitId, timestamp, timestamp);
        return items;
    }


    public String UpdateUser(App_user newUser) {
        String update = "UPDATE User_table " +
                "SET version = version + 1, name = ?" +
                "WHERE id = ?";
        App_user old_user = GetUserById(newUser.getId());
        if(newUser.getVersion()==old_user.getVersion()) {
            template.update(update, newUser.getName(), newUser.getId());
            return "User updated.";
        }else{
            return("Version mismatch.");
        }
    }
    public String CreateUser(App_user newUser) {
        String sql = "INSERT INTO User_table (id, version, name) VALUES (?,?,?)";
        App_user NEWUser = new App_user(newUser.getName());
        System.out.print("NEWUser:"+ NEWUser.getId() + "  " + NEWUser.getVersion() +"   "+NEWUser.getName());
        template.update(sql, NEWUser.getId(), NEWUser.getVersion(), NEWUser.getName());
        return "User created.";
    }

    public String DeleteUser(int UserId, int version) {
        String delete = "DELETE FROM User_table WHERE ID =?";
        String queryRole = "SELECT COUNT(*) " +
                "FROM UserRole_table " +
                "WHERE UserId = ?";
        App_user old_user = GetUserById(UserId);
        if(old_user.getVersion()==version) {
            Integer countRole = template.queryForObject(queryRole, Integer.class, UserId);
            if (countRole == 0) {
                template.update(delete, UserId);
                return "User deleted.";
            } else {
                return "Try to delete a user with user role, rejected.";
            }
        }else {
            return "Vesion mismatch.";
        }
    }


    public List<App_unit> listAllUnits() {
        String sql = "SELECT *, name FROM Unit_table";
        List<App_unit> items = template.query(sql,
                (result, rowNum) -> new App_unit(
                        result.getInt("id"),
                        result.getInt("version"),
                        result.getString("name"))
                );
        return items;
    }


    public List<App_role> listAllRoles() {
        String sql = "SELECT *, name FROM Role_table";
        List<App_role> items = template.query(sql,
                (result, rowNum) -> new App_role(
                        result.getInt("id"),
                        result.getInt("version"),
                        result.getString("name"))
                );
        return items;
    }


    public List<App_userRole> listUserRoleBy_user_unit(int userId, int unitId) {
        String sql = "SELECT * " +
                "FROM UserRole_table " +
                "WHERE UserId = ? AND UnitId = ?";

        List<App_userRole> items = template.query(sql,
                (result, rowNum) -> new App_userRole(
                        result.getInt("id"),
                        result.getInt("version"),
                        result.getInt("UserId"),
                        result.getInt("UnitId"),
                        result.getInt("RoleId"),
                        result.getTimestamp("ValidFrom"),
                        result.getTimestamp("ValidTo")
                ), userId, unitId);
        return items;
    }

    public List<App_userRole> listValidUserRoles(int userId, int unitId, Timestamp time) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date parsedDate = dateFormat.parse(String.valueOf(time));
        Timestamp timestamp = new Timestamp(parsedDate.getTime());

        String sql = "SELECT * " +
                "FROM UserRole_table " +
                "WHERE UserId = ? AND UnitId = ? AND ? > ValidFrom AND (? < ValidTo OR ValidTo IS NULL)";

        List<App_userRole> items = template.query(sql,
                (result, rowNum) -> new App_userRole(
                        result.getInt("id"),
                        result.getInt("version"),
                        result.getInt("UserId"),
                        result.getInt("UnitId"),
                        result.getInt("RoleId"),
                        result.getTimestamp("ValidFrom"),
                        result.getTimestamp("ValidTo")
                ), userId, unitId, timestamp, timestamp);
        return items;
    }

    public App_userRole GetUserRoleById(int id) {
        String sql = "SELECT * FROM UserRole_table WHERE ID=?";
        return template.queryForObject(sql,
                (result,
                 rowNum) -> new App_userRole(
                        result.getInt("id"),
                        result.getInt("version"),
                        result.getInt("UserId"),
                        result.getInt("UnitId"),
                        result.getInt("RoleId"),
                        result.getTimestamp("ValidFrom"),
                        result.getTimestamp("ValidTo")
                ), id);
    }

    public List<App_userRole> GetUserRoleByThree(int UserId, int UnitId, int RoleId) {
        String sql = "SELECT * FROM UserRole_table WHERE UserId=? AND UnitId = ? AND RoleId = ?";
        List<App_userRole> items = template.query(sql,
                (result,
                 rowNum) -> new App_userRole(
                        result.getInt("id"),
                        result.getInt("version"),
                        result.getInt("UserId"),
                        result.getInt("UnitId"),
                        result.getInt("RoleId"),
                        result.getTimestamp("ValidFrom"),
                        result.getTimestamp("ValidTo")
                ), UserId, UnitId, RoleId);
        return items;
    }

    public String UpdateUserRole(App_userRole newUserRole) {
        Timestamp current = new Timestamp(System.currentTimeMillis());
        if (newUserRole.getValidFrom()==null)
            newUserRole.setValidFrom(current);
        System.out.print("NEWUser:"+ newUserRole.getId() + "  " + newUserRole.getVersion() +"   "+ newUserRole.getUserId()+"  "+newUserRole.getUnitId()+" "+ newUserRole.getValidFrom()+ " "+ newUserRole.getValidTo());

        String update = "UPDATE UserRole_table " +
                "SET version = version + 1, ValidFrom = ?, ValidTo = ?" +
                "WHERE id = ?";
        App_userRole old_userRole = GetUserRoleById(newUserRole.getId());
        List<App_userRole> items = GetUserRoleByThree (newUserRole.getUserId(),newUserRole.getUnitId(),newUserRole.getRoleId());
        if (items.size() > 1) {
            for (App_userRole temp : items) {
                if (temp != null) {
                    if (temp.getValidTo() == null && newUserRole.getValidTo() != null) {
                        if (newUserRole.getValidTo().after(temp.getValidFrom())) {
                            return "Conflict valid time!";
                        }
                    } else if (newUserRole.getValidTo() == null ||
                            (newUserRole.getValidFrom().before(temp.getValidFrom()) && newUserRole.getValidTo().after(temp.getValidTo())) ||
                            (newUserRole.getValidTo().after(temp.getValidTo()) && newUserRole.getValidFrom().before(temp.getValidFrom()))) {
                        return "Conflict valid time!";
                    }
                }
            }
        }
        if(newUserRole.getVersion()==old_userRole.getVersion()) {
            if (newUserRole.getValidFrom()==null)
                newUserRole.setValidFrom(current);
            if (newUserRole.getValidTo() == null || newUserRole.getValidFrom().before(newUserRole.getValidTo())) {
                template.update(update, newUserRole.getValidFrom(), newUserRole.getValidTo(), newUserRole.getId());
                return "User role updated.";
            }else{
                return "Invalid time";
            }

        }else{
            return "Version mismatch.";
        }
    }
    public String CreateUserRole(App_userRole newUserRole) {
        Timestamp current = new Timestamp(System.currentTimeMillis());
        String sql = "INSERT INTO UserRole_table (id, version, UserId, UnitId, RoleId, ValidFrom, ValidTo) VALUES (?,?,?,?,?,?,?)";
        App_userRole NEWUserRole = new App_userRole(newUserRole.getUserId(), newUserRole.getUnitId(), newUserRole.getRoleId(), newUserRole.getValidFrom(), newUserRole.getValidTo());
        System.out.print("NEWUser:"+ NEWUserRole.getId() + "  " + NEWUserRole.getVersion() +"   "+ NEWUserRole.getUserId()+"  "+NEWUserRole.getUnitId());
        if (NEWUserRole.getValidFrom()==null)
            NEWUserRole.setValidFrom(current);

        List<App_userRole> items = GetUserRoleByThree (NEWUserRole.getUserId(),NEWUserRole.getUnitId(),NEWUserRole.getRoleId());
        for(App_userRole temp : items) {
            if (temp != null && temp.getId()!=NEWUserRole.getId()) {
                if (temp.getValidTo() == null && NEWUserRole.getValidTo() != null){
                    if(NEWUserRole.getValidTo().after(temp.getValidFrom())){
                        return "Conflict valid time!";
                    }
                }else if (NEWUserRole.getValidTo() == null ||
                        (NEWUserRole.getValidFrom().before(temp.getValidFrom()) && NEWUserRole.getValidTo().after(temp.getValidTo())) ||
                        (NEWUserRole.getValidTo().after(temp.getValidTo()) && NEWUserRole.getValidFrom().before(temp.getValidFrom())) ){
                    return "Conflict valid time!";
                }
            }
        }

        if (NEWUserRole.getValidTo() == null || newUserRole.getValidFrom().before(newUserRole.getValidTo())){
            template.update(sql, NEWUserRole.getId(), NEWUserRole.getVersion(), NEWUserRole.getUserId(), NEWUserRole.getUnitId(), NEWUserRole.getRoleId(), NEWUserRole.getValidFrom(), NEWUserRole.getValidTo());
            return "User role created.";
        }else{
            return "Invalid time";
        }


    }


    public String DeleteUserRole(int id, int version, int userId, int unitId) {
        String delete = "DELETE FROM UserRole_table WHERE id =? AND version = ? AND UserId = ? AND UnitId= ?";
        String queryRole = "SELECT COUNT(*) " +
                "FROM UserRole_table " +
                "WHERE UserId = ?";
        App_userRole old_userRole = GetUserRoleById(id);
        if(old_userRole.getVersion()==version) {
                template.update(delete, id, version, userId, unitId);
                return "User role deleted";
            } else{
            return "Vesion mismatch.";
        }
    }

    public List<App_user_userRole> getAllUserByUnitId(int unitId) {
        String sql = "SELECT DISTINCT User_table.id, User_table.version, User_table.name " +  //, UserRole_table.id, UserRole_table.version, UserRole_table.UserId, UserRole_table.UnitId, UserRole_table.RoleId, UserRole_table.ValidFrom, UserRole_table.ValidTo " +
                "FROM User_table " +
                "INNER JOIN UserRole_table ON User_table.id = UserRole_table.UserId " +
                "AND UserRole_table.UnitId = ? ";

        List<App_user> AppUser = template.query(sql,
                (result, rowNum) -> new App_user(
                        result.getInt("User_table.id"),
                        result.getInt("User_table.version"),
                        result.getString("User_table.name")
                ),
                unitId);

        String sql2 = "SELECT id, version, UserId, UnitId, RoleId, ValidFrom, ValidTo " +
                "FROM UserRole_table WHERE UnitId = ? AND UserId = ?";
        List<App_user_userRole> appUser_userRole = new ArrayList<App_user_userRole>();
        for(App_user temp : AppUser) {
            if (temp != null) {
                List<App_userRole> AppUserRole = template.query(sql2,
                        (result, rowNum) -> new App_userRole(
                                result.getInt("id"),
                                result.getInt("version"),
                                result.getInt("UserId"),
                                result.getInt("UnitId"),
                                result.getInt("RoleId"),
                                result.getTimestamp("ValidFrom"),
                                result.getTimestamp("ValidTo")
                        ),
                        unitId, temp.getId());
                App_user_userRole singleuserrole = new App_user_userRole(temp, AppUserRole);
                appUser_userRole.add(singleuserrole);
            }
        }
        return appUser_userRole;
    }
}