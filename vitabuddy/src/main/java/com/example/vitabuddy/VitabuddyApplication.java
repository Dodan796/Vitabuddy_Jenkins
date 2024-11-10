package com.example.vitabuddy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@ComponentScan(basePackages = { "com.example.vitabuddy" })
@MapperScan(basePackages = { "com.example.vitabuddy" })
@PropertySources({

@PropertySource(value={"file: c:/springBootWorkspace/webservice /configure.properties",
"file:/usr/local/project/properties/configure.properties", },
ignoreResourceNotFound=true)

})
public class VitabuddyApplication {

    public static void main(String[] args) {
        SpringApplication.run(VitabuddyApplication.class, args);
    }

}
