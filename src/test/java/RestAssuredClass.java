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
}
