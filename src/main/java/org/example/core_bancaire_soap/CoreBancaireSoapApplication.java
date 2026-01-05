package org.example.core_bancaire_soap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "org.example.core_bancaire_soap.client")
public class CoreBancaireSoapApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreBancaireSoapApplication.class, args);
    }

}
