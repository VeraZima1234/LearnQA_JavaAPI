
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;

import java.util.HashMap;
import java.util.Map;


public class HelloWorldTest {

    @Test
    public void testRestAssured()
    {
        Response response = RestAssured
                .given()
                .body("param1=value1&param2=value2") //body- параметры в теле запроса
                .post("https://playground.learnqa.ru/api/check_type")
                .andReturn();
        response.print();


        /*
        /////////////////////////////////////
        Map<String,String> data=new HashMap<>();
        data.put("login", "secret_login");
        data.put("password", "secret_pass");
        Response responseForGet = RestAssured
                .given()
                .body(data)
                .when()
                .post("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

        String responseCookie=responseForGet.getCookie("auth_cookie");

        // Передаем полученную куки
        Map<String,String > cookies=new HashMap<>();
        cookies.put("auth_cookie",responseCookie);

        Response responseForCheck= RestAssured
                .given()
                .body(data)
                .cookies(cookies)
                .when()
                .post("https://playground.learnqa.ru/api/check_auth_cookie")
                .andReturn();

        responseForCheck.print();

/////////////////////////////
         Map<String,String> data=new HashMap<>();
        data.put("login", "secret_login");
        data.put("password", "secret_pass");
        Response response = RestAssured
                .given()
                .body(data)
                .when()
                .post("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

        System.out.println("\nPretty text");
        response.prettyPrint();

        System.out.println("\nHeaders");
        Headers responseHeaders=response.getHeaders();
        System.out.println(responseHeaders);

        System.out.println("\nCookies");
        Map<String,String> responseCookies=response.getCookies();
        System.out.println(responseCookies);

        String responseCookie=response.getCookie("auth_cookie");
        System.out.println("\n "+responseCookie);

//////////////////////////////
        Map<String,String> headers=new HashMap<>();
        headers.put("myHeader1", "myValue1");
        headers.put("myHeader2", "myValue2");
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/get_303")
                .andReturn();

        response.prettyPrint(); //ответ сервера, в котором возвращается список полученных им в запросе заголовков
        String locationHeader=response.getHeader("Location"); // значение заголовка Location

        System.out.println(locationHeader);

        Map<String,String> headers=new HashMap<>();
        headers.put("myHeader1", "myValue1");
        headers.put("myHeader2", "myValue2");
        Response response = RestAssured
                .given()
                .headers(headers) //передаем заголовки в запрос
                .when()
                .get("https://playground.learnqa.ru/api/show_all_headers")
                .andReturn();

        response.prettyPrint(); //ответ сервера, в котором возвращается список полученных им в запросе заголовков
        Headers responseHeaders=response.getHeaders(); // список возвращаемых заголовков

        System.out.println(responseHeaders);

        Response response = RestAssured
                .given()
                .redirects()
                .follow(true)
                .when()
                .get("https://playground.learnqa.ru/api/get_303")
                .andReturn();
        int statusCode=response.getStatusCode();
        System.out.println(statusCode); //код 200, тк мы следуем по новой урл

        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/get_303")
                .andReturn();
        int statusCode=response.getStatusCode();
        System.out.println(statusCode); // код 303, тк мы не следуем по новой урл

        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/something")
                .andReturn();
        int statusCode=response.getStatusCode();
        System.out.println(statusCode);  // код 404

        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/check_type")
                .andReturn();
        int statusCode=response.getStatusCode();
        System.out.println(statusCode); // ответ код 200

        Map<String, Object> body =new HashMap<>();
        body.put("param1", "value1");
        body.put("param2", "value2");
        Response response = RestAssured
                .given()
                .body(body)
                .post("https://playground.learnqa.ru/api/check_type")
                .andReturn();
        response.print();

        Response response = RestAssured
                .given()
                .body("{\"param1\":\"value1\",\"param2\":\"value2\"")
                .post("https://playground.learnqa.ru/api/check_type")
                .andReturn();
        response.print();

        Response response = RestAssured
                .given()
                .body("param1=value1&param2=value2") //body- параметры в теле запроса
                .post("https://playground.learnqa.ru/api/check_type")
                .andReturn();
        response.print();

        Response response = RestAssured
                .given()
                .queryParam("param1", "value1")  //queryParam - это параметры get запроса, они передаются в урл. Это встроенный метод
                .queryParam("param2", "value2")
                .get("https://playground.learnqa.ru/api/check_type")
                .andReturn();
        response.print();

        Map<String, String> params=new HashMap<>();
        params.put("name", "John");
        JsonPath response = RestAssured
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();
        String answer=response.get("answer");
        System.out.println(answer);

        Response response = RestAssured
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/api/hello")
                .andReturn();
                response.prettyPrint();

                Response response1 = RestAssured
                .get("https://playground.learnqa.ru/api/hello")
                .andReturn();
                response1.prettyPrint();
         */
    }
}
