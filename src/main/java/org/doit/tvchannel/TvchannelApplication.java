package org.doit.tvchannel;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class TvchannelApplication {

	public static void main(String[] args) {
		SpringApplication.run(TvchannelApplication.class, args);
	}

}
