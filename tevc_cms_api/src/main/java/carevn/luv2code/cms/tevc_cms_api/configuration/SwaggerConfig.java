package carevn.luv2code.cms.tevc_cms_api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TEVC CMS API")
                        .version("1.0.0")
                        .description("Comprehensive API documentation for TEVC CMS")
                        .contact(new Contact()
                                .name("TEVC CMS Support")
                                .email("support@tevc-cms.com")
                                .url("https://tevc-cms.com")))
                .addTagsItem(new Tag()
                        .name("Authentication")
                        .description("Endpoints for user authentication and token management"))
                .addTagsItem(new Tag()
                        .name("User Management")
                        .description("Endpoints for managing user accounts and permissions"))
                .addTagsItem(new Tag()
                        .name("Role Management")
                        .description("Endpoints for managing roles and their permissions"))
                .addTagsItem(
                        new Tag().name("Training Management").description("Endpoints for managing training programs"))
                .addTagsItem(new Tag().name("Project Management").description("Endpoints for managing projects"))
                .addTagsItem(new Tag().name("Position Management").description("Endpoints for managing positions"))
                .addTagsItem(new Tag().name("Employee Management").description("Endpoints for managing employees"))
                .addTagsItem(new Tag().name("Department Management").description("Endpoints for managing departments"))
                .addTagsItem(new Tag().name("Contract Management").description("Endpoints for managing contracts"))
                .addTagsItem(new Tag().name("Other").description("Miscellaneous endpoints"));
    }
}
