package br.hela;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;

@Configuration 
@EnableAutoConfiguration 
@EnableSwagger 
@ComponentScan(basePackages = {"br.hela"})
public class Escoladeti2018Application {
	@Autowired
	SpringSwaggerConfig swaggerConfig;

	public static void main(String[] args) {
		new SpringApplicationBuilder(Escoladeti2018Application.class).web(true).run(args);
	}

	 @Bean
	    public SwaggerSpringMvcPlugin groupOnePlugin() { 
		 return new SwaggerSpringMvcPlugin(swaggerConfig)
	           .apiInfo(apiInfo()) 
	           .includePatterns("/alergia.*?", "/cirurgia.*?", "/doenca.*?", "/medicamentoContinuo.*?", "/usuario.*?") 
	           .swaggerGroup("admin");
	    }
	 
	private ApiInfo apiInfo() {
		ApiInfo apiInfo = new ApiInfo(
				 "Hela REST API",
	             "Essa é uma API usada pelo aplicativo Hela documentada pelo Swagger",
	             "Para integração entre em contato com a equipe Hela",
	             "hela@gmail.com",
	             "Licença Aberta",
	             "hela@gmail.com");
		return apiInfo;
	}
}