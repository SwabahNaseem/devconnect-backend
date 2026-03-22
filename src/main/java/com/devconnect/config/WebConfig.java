package com.devconnect.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * WebConfig — tells Spring to serve files from the "uploads/" folder.
 *
 * This means if a file is saved at uploads/profiles/image.jpg
 * it will be accessible at: http://localhost:8080/uploads/profiles/image.jpg
 *
 * The frontend can display images using this URL directly.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
            .addResourceHandler("/uploads/**")       // URL pattern
            .addResourceLocations("file:uploads/");  // folder on disk
    }
}