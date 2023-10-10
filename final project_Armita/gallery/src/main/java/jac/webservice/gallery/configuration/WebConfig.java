package jac.webservice.gallery.configuration;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;


@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String imagesDirectory = System.getProperty("user.dir") + "/src/main/resources/static/images/";
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:"+imagesDirectory)
                .setCachePeriod(0); // Disable caching for image resources
    }
}