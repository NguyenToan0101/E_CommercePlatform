package org.example.ecommerce.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableCaching
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

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("rootCategories", "childCategories", "allCategories");
    }
}
