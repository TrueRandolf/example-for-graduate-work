package ru.skypro.homework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${app.upload.main-dir}")
    private String mainDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        String rootPath = Paths.get(mainDir).toAbsolutePath().toUri().toString();

        if(!rootPath.endsWith("/")){
            rootPath+="/";
        }

        registry.addResourceHandler ("/images/**")
               .addResourceLocations(rootPath);
    }



    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // адрес фронта
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
