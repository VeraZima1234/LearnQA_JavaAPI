package tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("Authorization cases")
@Feature("Authorization")
public class UserEditTest extends BaseTestCase {

    @Test
    public void testEditJustCreatedTest(){

        Map<String,String> userData= DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth= RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();

        String userId=responseCreateAuth.getString("id");

        Map<String,String> authData=new HashMap<>();
        authData.put("email",userData.get("email"));
        authData.put("password",userData.get("password"));

        Response responseGetAuth=RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        String newName="Changed Name";
        Map<String,String> editData=new HashMap<>();
        editData.put("firstName",newName);

        Response responseEditUser=RestAssured
                .given()
                .header("x-csrf-token",this.getHeader(responseGetAuth,"x-csrf-token"))
                .cookie("auth_sid",this.getCookie(responseGetAuth,"auth_sid"))
                .body(editData)
                .put("https://playground.learnqa.ru/api/user/"+userId)
                .andReturn();

        Response responseUserData=RestAssured
                .given()
                .header("x-csrf-token",this.getHeader(responseGetAuth,"x-csrf-token"))
                .cookie("auth_sid",this.getCookie(responseGetAuth,"auth_sid"))
                .get("https://playground.learnqa.ru/api/user/"+userId)
                .andReturn();

        Assertions.assertJsonByName(responseUserData,"firstName",newName);

    }

    @Test
    public void testEditJustCreatedTestWithoutAuthorization(){

        //Create
        Map<String,String> userData= DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth= new ApiCoreRequests().makePostRequest("https://playground.learnqa.ru/api/user/",userData).jsonPath();

        String userId=responseCreateAuth.getString("id");


        //Edit
        String newName="Changed Name";
        Map<String,String> editData=new HashMap<>();
        editData.put("firstName",newName);

        Response responseEditUser=new ApiCoreRequests()
                .makePutRequest(
                        "https://playground.learnqa.ru/api/user/"+userId,
                        editData);

        //Login
        Map<String,String> authData=new HashMap<>();
        authData.put("email",userData.get("email"));
        authData.put("password",userData.get("password"));

        Response responseGetAuth=new ApiCoreRequests().makePostRequest("https://playground.learnqa.ru/api/user/login",authData);

        //Get user data
        Response responseUserData=new ApiCoreRequests()
                .makeGetRequest(
                        "https://playground.learnqa.ru/api/user/"+userId,
                        this.getHeader(responseGetAuth,"x-csrf-token"),
                        this.getCookie(responseGetAuth,"auth_sid"));

        Assertions.assertJsonByName(responseUserData,"firstName",userData.get("firstName"));

    }

    @Test
    public void testEditJustCreatedTestWithAuthorizationAsOther(){

        //Create
        Map<String,String> userData= DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth= new ApiCoreRequests().makePostRequest("https://playground.learnqa.ru/api/user/",userData).jsonPath();

        String userId=responseCreateAuth.getString("id");


        //Login
        Map<String,String> authData=new HashMap<>();
        authData.put("email","vinkotov@example.com");
        authData.put("password","1234");

        Response responseGetAuth=new ApiCoreRequests().makePostRequest("https://playground.learnqa.ru/api/user/login",authData);

        //Edit
        String newName="Changed Name";
        Map<String,String> editData=new HashMap<>();
        editData.put("firstName",newName);

        Response responseEditUser=new ApiCoreRequests()
                .makePutRequest(
                        "https://playground.learnqa.ru/api/user/"+userId,
                        this.getHeader(responseGetAuth,"x-csrf-token"),
                        this.getCookie(responseGetAuth,"auth_sid"),
                        editData);

        //Login
        Map<String,String> authData1=new HashMap<>();
        authData1.put("email",userData.get("email"));
        authData1.put("password",userData.get("password"));

        Response responseGetAuth1=new ApiCoreRequests().makePostRequest("https://playground.learnqa.ru/api/user/login",authData1);

        //Get user data
        Response responseUserData=new ApiCoreRequests()
                .makeGetRequest(
                        "https://playground.learnqa.ru/api/user/"+userId,
                        this.getHeader(responseGetAuth1,"x-csrf-token"),
                        this.getCookie(responseGetAuth1,"auth_sid"));

        Assertions.assertJsonByName(responseUserData,"firstName",userData.get("firstName"));

    }

    @Test
    public void testEditJustCreatedTestWithInvalidEmail(){

        //Create
        Map<String,String> userData= DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth= new ApiCoreRequests().makePostRequest("https://playground.learnqa.ru/api/user/",userData).jsonPath();

        String userId=responseCreateAuth.getString("id");


        //Login
        Map<String,String> authData=new HashMap<>();
        authData.put("email",userData.get("email"));
        authData.put("password",userData.get("password"));

        Response responseGetAuth=new ApiCoreRequests().makePostRequest("https://playground.learnqa.ru/api/user/login",authData);

        //Edit
        String newEmail=DataGenerator.getRandomEmail().replace("@","");
        Map<String,String> editData=new HashMap<>();
        editData.put("email",newEmail);

        Response responseEditUser=new ApiCoreRequests()
                .makePutRequest(
                        "https://playground.learnqa.ru/api/user/"+userId,
                        this.getHeader(responseGetAuth,"x-csrf-token"),
                        this.getCookie(responseGetAuth,"auth_sid"),
                        editData);

        //Get user data
        Response responseUserData=new ApiCoreRequests()
                .makeGetRequest(
                        "https://playground.learnqa.ru/api/user/"+userId,
                        this.getHeader(responseGetAuth,"x-csrf-token"),
                        this.getCookie(responseGetAuth,"auth_sid"));

        Assertions.assertJsonByName(responseUserData,"email",userData.get("email"));

    }

    @Test
    public void testEditJustCreatedTestWithShortFirstName(){

        //Create
        Map<String,String> userData= DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth= new ApiCoreRequests().makePostRequest("https://playground.learnqa.ru/api/user/",userData).jsonPath();

        String userId=responseCreateAuth.getString("id");


        //Login
        Map<String,String> authData=new HashMap<>();
        authData.put("email",userData.get("email"));
        authData.put("password",userData.get("password"));

        Response responseGetAuth=new ApiCoreRequests().makePostRequest("https://playground.learnqa.ru/api/user/login",authData);

        //Edit
        Map<String,String> editData=new HashMap<>();
        editData.put("firstName","f");

        Response responseEditUser=new ApiCoreRequests()
                .makePutRequest(
                        "https://playground.learnqa.ru/api/user/"+userId,
                        this.getHeader(responseGetAuth,"x-csrf-token"),
                        this.getCookie(responseGetAuth,"auth_sid"),
                        editData);

        //Get user data
        Response responseUserData=new ApiCoreRequests()
                .makeGetRequest(
                        "https://playground.learnqa.ru/api/user/"+userId,
                        this.getHeader(responseGetAuth,"x-csrf-token"),
                        this.getCookie(responseGetAuth,"auth_sid"));

        Assertions.assertJsonByName(responseUserData,"firstName",userData.get("firstName"));

    }
}
