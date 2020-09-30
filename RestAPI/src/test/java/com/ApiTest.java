package com;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    //1. List all users
    @Test
    public void testListAllUser() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/api/user",
                String.class)).contains("Alice");
    }
    //2. List all users with at least one valid user role at a given unit at a given time
    @Test
    public void testListAllUserByUnit() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/api/user/11/2019-12-31 23:59:00",
                String.class)).contains("[{\"id\":1,\"version\":1,\"name\":\"Alice\"}]");
    }

    //3. List all units
    @Test
    public void testListAllUnit()throws Exception
    {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/api/unit",
                String.class)).contains("Kreftregisteret");
    }
    //4. List all roles
    @Test
    public void testListAllRole()throws Exception
    {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/api/role",
                String.class)).contains("User administration");
    }

    //5. List all user roles (both currently valid and invalid) for a given user id and unit id
    @Test
    public void testListAllUserRoleById()throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/api/userrole/1/11",
                String.class)).contains("[{\"id\":1001,\"version\":1,\"userId\":1,\"unitId\":11,\"roleId\":101,\"validTo\":\"2019-12-31T22:59:59.000+00:00\",\"validFrom\":\"2019-01-01T23:00:00.000+00:00\"},{\"id\":1002,\"version\":2,\"userId\":1,\"unitId\":11,\"roleId\":104,\"validTo\":\"2019-12-31T22:59:59.000+00:00\",\"validFrom\":\"2019-01-01T23:00:00.000+00:00\"},{\"id\":1003,\"version\":1,\"userId\":1,\"unitId\":11,\"roleId\":105,\"validTo\":\"2019-12-31T22:59:59.000+00:00\",\"validFrom\":\"2019-06-10T22:00:00.000+00:00\"},{\"id\":1008,\"version\":1,\"userId\":1,\"unitId\":11,\"roleId\":101,\"validTo\":null,\"validFrom\":\"2020-01-27T23:00:00.000+00:00\"},{\"id\":1009,\"version\":1,\"userId\":1,\"unitId\":11,\"roleId\":104,\"validTo\":null,\"validFrom\":\"2020-01-27T23:00:00.000+00:00\"}]");
    }
    //6. List only valid user roles for a given user id and unit id at a given timestamp
    @Test
    public void testListAllValidUserRole()throws Exception
    {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/api/userrole/1/11/2028-01-28 01:00:00",
                String.class)).contains("[{\"id\":1008","{\"id\":1009");
    }
    //7. Create a new user
    @Test
    public void testCreatUser()throws Exception
    {
        String Url = "http://localhost:" + port + "/api/user";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject testObj = new JSONObject();
        testObj.put("name", "John");
        HttpEntity<String> request =
                new HttpEntity<String>(testObj.toString(), headers);

        assertThat(restTemplate.postForObject(Url, request, String.class)).contains("User created.");
    }
    //8.Update an existing user
    @Test
    public void testUpdateUser()throws Exception
    {
        String Url = "http://localhost:" + port + "/api/user";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject testObj = new JSONObject();
        testObj.put("id", "1");
        testObj.put("version", "1");
        testObj.put("name", "Alice");
        HttpEntity<String> request =
                new HttpEntity<String>(testObj.toString(), headers);

        assertThat(restTemplate.postForObject(Url, request, String.class)).contains("User updated.");
    }
    //9. Delete an existing user. A user can only be deleted if there are no user roles for that user.
    @Test
    public void testDeletUser()throws Exception
    {
        String Url = "http://localhost:" + port + "/api/user/1/1";
        restTemplate.delete(Url);
    }
    // 10. Create a new user role for a given user id, unit id, role id, an optional valid from timestamp (if not specified, default to the current date and time)
    //and an optional valid to timestamp (if not specified, default to no timestamp). If a valid to timestamp is specified, it must be after the valid from
    //timestamp (or the current date and time if valid from timestamp is not specified in the request). At most one user role for a given combination of
    //user id, unit id and role id can be valid at any point in time.
    @Test
    public void testCreatUserRole()throws Exception
    {
        String Url = "http://localhost:" + port + "/api/userrole";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //Successful create
        JSONObject testObj = new JSONObject();
        testObj.put("UserId", "1");
        testObj.put("UnitId", "11");
        testObj.put("RoleId", "106");
        testObj.put("ValidFrom", null);
        testObj.put("ValidTo", null);
        HttpEntity<String> request =
                new HttpEntity<String>(testObj.toString(), headers);

        assertThat(restTemplate.postForObject(Url, request, String.class)).contains("User role created.");

        //Failure create
        JSONObject testObj2 = new JSONObject();
        testObj2.put("UserId", "1");
        testObj2.put("UnitId", "11");
        testObj2.put("RoleId", "104");
        testObj2.put("ValidFrom", null);
        testObj2.put("ValidTo", null);
        HttpEntity<String> request2 =
                new HttpEntity<String>(testObj2.toString(), headers);

        assertThat(restTemplate.postForObject(Url, request2, String.class)).contains("Conflict valid time!");

    }
    //11.Update an existing user role. Only the valid from and valid to timestamps can be changed. The valid from timestamp, if specified, must be a
    //timestamp (a user role must always have a valid from timestamp). The requirement that the valid to timestamp, if specified, must come after the
    //valid from timestamp must be enforced, and an update that would cause two user roles for the same user id, unit id and role id to be valid at the
    //same time must be rejected.
    @Test
    public void testUpdateUserRole()throws Exception
    {
        String Url = "http://localhost:" + port + "/api/userrole";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //Successful create
        JSONObject testObj = new JSONObject();
        testObj.put("id", "1007");
        testObj.put("version", "1");
        testObj.put("userId", "2");
        testObj.put("unitId", "14");
        testObj.put("roleId", "102");
        testObj.put("validFrom", "2020-01-27T23:00:00.000+00:00");
        testObj.put("validTo", null);
        HttpEntity<String> request =
                new HttpEntity<String>(testObj.toString(), headers);

        assertThat(restTemplate.postForObject(Url, request, String.class)).contains("User role updated.");

        //Failure create
        JSONObject testObj2 = new JSONObject();
        testObj2.put("id", "1001");
        testObj2.put("version", "1");
        testObj2.put("userId", "1");
        testObj2.put("unitId", "11");
        testObj2.put("roleId", "101");
        testObj2.put("validFrom", null);
        testObj2.put("validTo", null);
        HttpEntity<String> request2 =
                new HttpEntity<String>(testObj2.toString(), headers);

        assertThat(restTemplate.postForObject(Url, request2, String.class)).contains("Conflict valid time!");

    }
    //12.Delete an existing user role.
    @Test
    public void testDeletUserRole()throws Exception
    {
        String Url = "http://localhost:" + port + "/api/userrole/1001/1/1/11";
        restTemplate.delete(Url);
    }

    //13.For a given unit id, list all users with at least one user role at that unit (whether the user role is currently valid or not), and for each user, list all of
    //the user's user roles at the given unit id. If application/xml is specified in the Accept header of the request, the message body of the response
    //should be valid XML. If application/json is specified in the Accept header of the request, the message body of the response should be as JSON
    @Test
    public void testListUserRoleByUnit()throws Exception
    {
        String Url = "http://localhost:" + port + "/api/userrole/11";
        assertThat(this.restTemplate.getForObject(Url,String.class)).contains("[{\"app_user\":{\"id\":1,\"version\":1,\"name\":\"Alice\"},","\"app_userRole\":[{\"id\":1001,\"version\":1,","{\"id\":1002,\"version\":2,","{\"id\":1003,\"version\":1,","{\"id\":1008,\"version\":1");
    }
}