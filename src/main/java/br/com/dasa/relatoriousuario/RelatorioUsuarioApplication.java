package br.com.dasa.relatoriousuario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class RelatorioUsuarioApplication extends SpringBootServletInitializer {

	 @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(RelatorioUsuarioApplication.class);
    }
	
	public static void main(String[] args) {
		SpringApplication.run(RelatorioUsuarioApplication.class, args);
	}
	
}
