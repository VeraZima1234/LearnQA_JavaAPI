import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.util.*;

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

    @Test
    public void testFindPassword()
    {

        String[] passwords = {"password","123456","12345678","qwerty","abc123","monkey","1234567","letmein",
                "trustno1","dragon","baseball","111111","iloveyou","master","sunshine","ashley","bailey",
                "passw0rd","shadow","123123","654321","superman","qazwsx","michael","Football","password",
                "123456","12345678","abc123","qwerty","monkey","letmein","dragon","111111","baseball",
                "iloveyou","trustno1","1234567","sunshine","master","123123","welcome","shadow","ashley",
                "football","jesus","michael","ninja","mustang","password1","123456","password","12345678",
                "qwerty","abc123","123456789","111111","1234567","iloveyou","adobe123","123123","admin",
                "1234567890","letmein","photoshop","1234","monkey","shadow","sunshine","12345","password1",
                "princess","azerty","trustno1","0","123456","password","12345","12345678","qwerty","123456789",
                "1234","baseball","dragon","football","1234567","monkey","letmein","abc123","111111","mustang",
                "access","shadow","master","michael","superman","696969","123123","batman","trustno1","123456",
                "password","12345678","qwerty","12345","123456789","football","1234","1234567","baseball","welcome",
                "1234567890","abc123","111111","1qaz2wsx","dragon","master","monkey","letmein","login","princess",
                "qwertyuiop","solo","passw0rd","starwars","123456","password","12345","12345678","football","qwerty",
                "1234567890","1234567","princess","1234","login","welcome","solo","abc123","admin","121212","flower",
                "passw0rd","dragon","sunshine","master","hottie","loveme","zaq1zaq1","password1","123456","password",
                "12345678","qwerty","12345","123456789","letmein","1234567","football","iloveyou","admin","welcome",
                "monkey","login","abc123","starwars","123123","dragon","passw0rd","master","hello","freedom","whatever",
                "qazwsx","trustno1","123456","password","123456789","12345678","12345","111111","1234567","sunshine",
                "qwerty","iloveyou","princess","admin","welcome","666666","abc123","football","123123","monkey","654321",
                "!@#$%^&*","charlie","aa123456","donald","password1","qwerty123","123456","123456789","qwerty","password",
                "1234567","12345678","12345","iloveyou","111111","123123","abc123","qwerty123","1q2w3e4r","admin","qwertyuiop",
                "654321","555555","lovely","7777777","welcome","888888","princess","dragon","password1","123qwe",};

        Object[] uniquePasswords= Arrays.stream(passwords).distinct().toArray();
        String login ="super_admin";
        String serviceUrl = "https://playground.learnqa.ru/ajax/api/get_secret_password_homework";
        String cookieUrl = "https://playground.learnqa.ru/ajax/api/check_auth_cookie";
        String authCookie="auth_cookie";

        Integer i=0;
        String answer="You are NOT authorized";
        while(!(answer.equals("You are authorized"))&&(i<uniquePasswords.length)) {
            Map<String, String> data = new HashMap<>();
            data.put("login", login);
            data.put("password", uniquePasswords[i].toString());
            Response response = RestAssured
                    .given()
                    .body(data)
                    .when()
                    .post(serviceUrl)
                    .andReturn();
            String responseCookie = response.getCookie(authCookie);

            Map<String,String > cookies=new HashMap<>();
            cookies.put(authCookie,responseCookie);
            Response cookieResponse= RestAssured
                    .given()
                    .body(data)
                    .cookies(cookies)
                    .when()
                    .post(cookieUrl)
                    .andReturn();

            answer=cookieResponse.asString();
            if(answer.equals("You are authorized")) {
                System.out.println("Correct password is '"+uniquePasswords[i].toString()+"'");
                break;
            }
            i++;
        }

    }
}
