package nl.ing.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LoginRequestTest {

    @Test
    public void login_request_should_convert_to_json_correctly() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(new LoginRequest("user", "password"));

        assertEquals("{\r\n" +
                "  \"username\" : \"user\",\r\n" +
                "  \"password\" : \"password\"\r\n" +
                "}", json);
    }

}