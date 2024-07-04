package you_tube_own.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class SwaggerConfig {
    @Value("${server.url}")
    private String url;

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(url);
        devServer.setDescription("You tube test project");

        Contact contact = new Contact();
        contact.setEmail("takhir.ismailoff@gmail.com");
        contact.setName("Takhir Ismailov");
        contact.setUrl("https://www.dasturlash.uz");

        Info info = new Info()
                .title("youtube Management API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage tutorials.")
                .termsOfService("https://www.dasturlash.uz")
                .license(null);

        return new OpenAPI().info(info).servers(List.of(devServer));
    }
}
