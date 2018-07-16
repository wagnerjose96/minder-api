package br.minder;

import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MinderApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("T03:00"));
		SpringApplication.run(MinderApplication.class, args); 
	}
}