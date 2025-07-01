package org.example.ecommerce.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConfigCloudinary {
    @Bean
    public Cloudinary configKey(){
        Map<String,String> config = new HashMap<>();
        config.put("cloud_name", "dhqoofpfa");
        config.put("api_key", "411532182384238");
        config.put("api_secret", "iqg9ZHMvWC5o3X2PBX60wFEExe8");
        return new Cloudinary(config);
    }
}
