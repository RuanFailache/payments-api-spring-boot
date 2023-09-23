package dev.payments.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfiguration {

    @Bean
    public OpenAPI api() {

        var apiInfo = new Info()
                .title("Ruan Failache")
                .description("Desafio técnico, api de pagamentos, para <strong>Pessoa Desenvolvedora Java</strong> na <strong>FADESP</strong>");

        return new OpenAPI()
                .info(apiInfo);

    }

}
