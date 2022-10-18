package tests;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserDeleteTest extends BaseTestCase {

    @Test
    public void testDeleteExistingUser(){

        //Login
        String email="vinkotov@example.com";
        Map<String,String> authData=new HashMap<>();
        authData.put("email",email);
        authData.put("password","1234");

        Response responseGetAuth=new ApiCoreRequests().makePostRequest("https://playground.learnqa.ru/api/user/login",authData);

        //Delete
        Response responseDeleteUser=new ApiCoreRequests()
                .makeDeleteRequest(
                        "https://playground.learnqa.ru/api/user/2",
                        this.getHeader(responseGetAuth,"x-csrf-token"),
                        this.getCookie(responseGetAuth,"auth_sid"));

        //Get user data
        Response responseUserData=new ApiCoreRequests()
                .makeGetRequest(
                        "https://playground.learnqa.ru/api/user/2",
                        this.getHeader(responseGetAuth,"x-csrf-token"),
                        this.getCookie(responseGetAuth,"auth_sid"));

        Assertions.assertJsonByName(responseUserData,"email",email);

    }

    @Test
    public void testDeleteJustCreatedUser(){

        //Create
        Map<String,String> userData= DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth= new ApiCoreRequests().makePostRequest("https://playground.learnqa.ru/api/user/",userData).jsonPath();

        String userId=responseCreateAuth.getString("id");


        //Login
        Map<String,String> authData=new HashMap<>();
        authData.put("email",userData.get("email"));
        authData.put("password",userData.get("password"));

        Response responseGetAuth=new ApiCoreRequests().makePostRequest("https://playground.learnqa.ru/api/user/login",authData);

        //Delete
        Response responseDeleteUser=new ApiCoreRequests()
                .makeDeleteRequest(
                        "https://playground.learnqa.ru/api/user/"+userId,
                        this.getHeader(responseGetAuth,"x-csrf-token"),
                        this.getCookie(responseGetAuth,"auth_sid"));

        //Get user data
        Response responseUserData=new ApiCoreRequests()
                .makeGetRequest(
                        "https://playground.learnqa.ru/api/user/"+userId,
                        this.getHeader(responseGetAuth,"x-csrf-token"),
                        this.getCookie(responseGetAuth,"auth_sid"));

        Assertions.assertResponseTextEquals(responseUserData,"User not found");

    }

    @Test
    public void testDeleteJustCreatedUserAuthorizationAsOtherUser(){

        //Create
        Map<String,String> userData= DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth= new ApiCoreRequests().makePostRequest("https://playground.learnqa.ru/api/user/",userData).jsonPath();

        String userId=responseCreateAuth.getString("id");


        //Login
        String email="vinkotov@example.com";
        Map<String,String> authData=new HashMap<>();
        authData.put("email",email);
        authData.put("password","1234");

        Response responseGetAuth=new ApiCoreRequests().makePostRequest("https://playground.learnqa.ru/api/user/login",authData);

        //Delete
        Response responseDeleteUser=new ApiCoreRequests()
                .makeDeleteRequest(
                        "https://playground.learnqa.ru/api/user/"+userId,
                        this.getHeader(responseGetAuth,"x-csrf-token"),
                        this.getCookie(responseGetAuth,"auth_sid"));

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

        System.out.println(responseUserData.asString());
        Assertions.assertJsonByName(responseUserData,"email",userData.get("email"));

    }


}
