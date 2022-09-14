import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.sql.Time;
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

    @Test
    public void testToken()
    {
        //первый запрос без токена
        JsonPath response= RestAssured
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        // получение токена и время задержки
        String token=response.get("token");
        Integer taskTime=response.get("seconds");

        // первый запрос с токеном
        JsonPath responseWithToken= RestAssured
                .given()
                .queryParam("token",token)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        // проверка корректности статуса
        String taskStatus=responseWithToken.get("status");
        if(taskStatus.equals("Job is NOT ready"))
            System.out.println("1st Status is correct");
        else System.out.println("1st Status is wrong: " +taskStatus);

        // ожидание времени задержки
        try {
            Thread.sleep(taskTime*1000);
        }catch(Exception e)
        {
            e.printStackTrace();
        }

        // второй запрос с токеном
        responseWithToken= RestAssured
                .given()
                .queryParam("token",token)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        // проверка статуса
        taskStatus=responseWithToken.get("status");
        if(taskStatus.equals("Job is ready"))
            System.out.println("2nd Status is correct");
        else System.out.println("2nd Status is wrong: " +taskStatus);

        // проверка наличия результата
        if(responseWithToken.get("result")!=null)
            System.out.println("Result is present");
        else
            System.out.println("Result is absent");
    }
}
