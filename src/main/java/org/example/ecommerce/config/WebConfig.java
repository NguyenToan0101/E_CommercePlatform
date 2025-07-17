package org.example.ecommerce.config;

import org.example.ecommerce.service.admin.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${api.frontend.seller}")
    private String API_FRONTEND_SELLER;
    @Value("${api.frontend.admin}")
    private String API_FRONTEND_ADMIN;
    @Bean
    public WebMvcConfigurer corsConfig(){
        return new WebMvcConfigurer() {
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(API_FRONTEND_SELLER,API_FRONTEND_ADMIN)
                        .allowedMethods("*")
                        .allowCredentials(true);


            }
        };
    }
    @Autowired
    private VisitService visitService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new VisitInterceptor(visitService));
    }
}
