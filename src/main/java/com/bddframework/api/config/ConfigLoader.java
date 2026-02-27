package com.bddframework.api.config;

import org.apache.groovy.json.internal.IO;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {
    private static Properties properties=new Properties();
    static{
        try(FileInputStream fis=new FileInputStream("src/test/resources/configuration.properties")){
            properties.load(fis);
        }catch (IOException error){
            throw new RuntimeException("Failed to load File");
        }
    }

    public static String getBaseUri(){
            return properties.get("baseUri").toString();
    }

    public static String getProperty(String key){
        return properties.get(key).toString();
    }
}
