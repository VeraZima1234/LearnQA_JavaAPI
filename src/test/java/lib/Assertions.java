package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.*;

public class Assertions {

    public static void assertJsonByName(Response Response, String name, int expectedValue){
        Response.then().assertThat().body("$",hasKey(name));
        int value = Response.jsonPath().getInt(name);
        assertEquals(expectedValue,value,"JSON value is wrong");
    }
}
