package com.aj.foodapp.configuration;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("foodapp.storage")
@Component
public class FileStorageConfig {

//    private String location = "D:/SpringBoot React Projects/foodapp_images/";

    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
