package common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(proxyBeanMethods = false)
public class MyCorsConfiguration {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**");
                registry.addMapping("/api/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET","POST","PUT", "DELETE")
                        .allowedHeaders("header", "header2", "header3")
                        .exposedHeaders("header", "header2")
                        .allowCredentials(true).maxAge(3600);
            }

        };
    }

}

