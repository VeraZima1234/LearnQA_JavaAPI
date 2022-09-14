import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

public class RestAssuredClass {

    @Test
    public void testJSONParsing()
    {
        JsonPath response= RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();


        ArrayList<Map<String,String>>  responseList=response.get("messages");
        System.out.println(responseList.get(1).get("message"));
    }

    @Test
    public void testRedirect()
    {
        Response response= RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        System.out.println(response.getHeader("Location"));
    }

    @Test
    public void testLongRedirect()
    {
        Integer redirectsCount=0;
        Response response= RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        String redirectURL=response.getHeader("Location");
        while(response.getStatusCode()!=200)
        {
            redirectsCount++;
            System.out.println(redirectURL);

            response= RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(redirectURL)
                    .andReturn();

            redirectURL=response.getHeader("Location");
        }
        System.out.println("Redirects Count = "+redirectsCount);
    }
}
