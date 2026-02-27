package com.bddframework.api.auth;

import com.bddframework.api.config.ConfigLoader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class AuthUtil {

    private AuthUtil() {
    }

    private static String token;

    public static String getToken() throws JsonProcessingException {
        if(token==null){
            token = fetchToken();
        }
        return token;
    }

    private static String fetchToken() throws JsonProcessingException {
        Response response=RestAssured.given()
                .baseUri(ConfigLoader.getBaseUri())
                .contentType("application/json")
                .body(getTokenBody())
                .post("/auth");
        return  response.jsonPath().getString("token");
    }

    private static String getTokenBody() throws JsonProcessingException {
            Map<String, String> map=new HashMap<>();
        String username = ConfigLoader.getProperty("username");
        String password = ConfigLoader.getProperty("password");
        map.put("username",username);
        map.put("password",password);
        ObjectMapper mapper=new ObjectMapper();
        return mapper.writeValueAsString(map);

    }
}
