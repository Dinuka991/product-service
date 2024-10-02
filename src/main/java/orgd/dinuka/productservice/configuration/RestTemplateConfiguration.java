package orgd.dinuka.productservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


/**
 * @Configuration annotation in spring indicates that class is a configuration class that contains spring bean definitiosn. it tells springs
 * that the class contains one or more method annotated with @Bean, which define beans for spring application context.
 */

@Configuration
public class RestTemplateConfiguration {


    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
