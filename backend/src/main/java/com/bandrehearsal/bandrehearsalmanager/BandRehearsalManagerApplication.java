package com.bandrehearsal.bandrehearsalmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class BandRehearsalManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BandRehearsalManagerApplication.class, args);
    }

}
