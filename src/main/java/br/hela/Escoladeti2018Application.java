package br.hela;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan(basePackages = { "br.hela" })
public class Escoladeti2018Application {

	public static void main(String[] args) {
		new SpringApplicationBuilder(Escoladeti2018Application.class).run(args);
	}

}