package com.gym.gymmanagementsystem;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${upload.profile-photos}")
    private String uploadDir;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Povolíme CORS pro /api/**
        // a konkrétní origin http://localhost:5173
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Např. /profile-photos/** => soubory z /var/gymapp/uploads/profile-photos/
        registry.addResourceHandler("/profile-photos/**")
                .addResourceLocations("file:" + uploadDir + "/");
        // "file:" prefix říká Springu, že se jedná o souborový systém
    }

}
