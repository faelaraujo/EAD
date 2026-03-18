package com.ead.configserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {

    @Value("${GIT_USERNAME:NAO_ENCONTRADO}")
    private String username;

    @Value("${GIT_TOKEN:NAO_ENCONTRADO}")
    private String token;

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);


    }

    @Bean
    public CommandLineRunner printEnv(Environment env) {
        return args -> {
            System.out.println("Property sources:");

            ((org.springframework.core.env.AbstractEnvironment) env)
                    .getPropertySources()
                    .forEach(ps -> System.out.println(ps.getName()));

            String username = env.getProperty("GIT_USERNAME", "NAO_ENCONTRADO");
            String token = env.getProperty("GIT_TOKEN", "NAO_ENCONTRADO");

            System.out.println("GIT_USERNAME: " + username);

            if (!token.equals("NAO_ENCONTRADO") && token.length() > 4) {
                System.out.println("GIT_TOKEN (real): " + token); // debug temporário
            } else {
                System.out.println("GIT_TOKEN: NAO_ENCONTRADO");
            }
        };
    }


}
