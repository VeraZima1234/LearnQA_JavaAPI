package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserGetTest extends BaseTestCase {
    @Test
    public void testGetUserDataNotAuth(){
        Response responseUserData= RestAssured
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();

        System.out.println(responseUserData.asString());
        Assertions.assertJsonHasNotField(responseUserData,"firstName");
        Assertions.assertJsonHasNotField(responseUserData,"lastName");
        Assertions.assertJsonHasNotField(responseUserData,"email");
    }

    @Test
    public void testGetUserDetailsAuthAsSameUser(){
        Map<String,String> authData= new HashMap<>();
        authData.put("email","vinkotov@example.com");
        authData.put("password","1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
        String header=this.getHeader(responseGetAuth,"x-csrf-token");
        String cookie=this.getCookie(responseGetAuth,"auth_sid");

        Response responseUserData=RestAssured
                .given()
                .header("x-csrf-token",header)
                .cookie("auth_sid",cookie)
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();

        String[] expectedFields={"username","firstName","lastName","email"};
        Assertions.assertJsonHasFields(responseUserData,expectedFields);

    }

    @Test
    public void testGetUserDetailsAuthAsOtherUser(){
        Map<String,String> authData= new HashMap<>();
        authData.put("email","vinkotov@example.com");
        authData.put("password","1234");

        Response responseGetAuth = new ApiCoreRequests().makePostRequest("https://playground.learnqa.ru/api/user/login",authData);
        String header=this.getHeader(responseGetAuth,"x-csrf-token");
        String cookie=this.getCookie(responseGetAuth,"auth_sid");

        Response responseUserData=new ApiCoreRequests().makeGetRequest("https://playground.learnqa.ru/api/user/1",header,cookie);

        String[] unexpectedFields={"firstName","lastName","email"};
        Assertions.assertJsonHasField(responseUserData,"username");
        Assertions.assertJsonHasNotFields(responseUserData,unexpectedFields);

    }
}
