package com.bddframework.api.auth;

import com.bddframework.api.config.ConfigLoader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AuthUtil {

    private AuthUtil() {
    }

    private static String token;

    public static String getToken() throws JsonProcessingException {
        if(token==null){
            log.debug("Token is null");
            token = fetchToken();
        }
        log.info("returning token");
        return token;
    }

    private static String fetchToken() throws JsonProcessingException {
        Response response=RestAssured.given()
                .baseUri(ConfigLoader.getBaseUri())
                .contentType("application/json")
                .body(getTokenBody())
                .post("/auth");
        log.debug("Fetching token from response");
        return  response.jsonPath().getString("token");
    }

    private static String getTokenBody() throws JsonProcessingException {
        log.debug("Generating token api payload from configuration file credentials");
            Map<String, String> map=new HashMap<>();
        String username = ConfigLoader.getProperty("username");
        String password = ConfigLoader.getProperty("password");
        map.put("username",username);
        map.put("password",password);
        ObjectMapper mapper=new ObjectMapper();
        return mapper.writeValueAsString(map);
    }
}
