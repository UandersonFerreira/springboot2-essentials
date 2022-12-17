package estudo.java.springboot2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class Springboot2EssentialsApplication {

	public static void main(String[] args) {
		SpringApplication.run(Springboot2EssentialsApplication.class, args);
	}

}
/*
Anotações basicas para a geração do serviço spring:

@EnableAutoConfiguration
@ComponentScan
@Configuration

-@SpringBootApplication: Implementa as 3 anotações

 */
