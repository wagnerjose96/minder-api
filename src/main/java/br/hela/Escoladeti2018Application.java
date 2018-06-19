package br.hela;

import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Escoladeti2018Application {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("T03:00"));
		SpringApplication.run(Escoladeti2018Application.class, args); 
	}
}