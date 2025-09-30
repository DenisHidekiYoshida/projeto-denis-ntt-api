package br.com.ntt.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "br.com.ntt.bank")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
