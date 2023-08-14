package com.example.ct2.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
@EnableWebMvc
public class MvcConfiguration implements WebMvcConfigurer {

    @Value("${resource.path}")
    private String resourcePath;

    @Value("${images.path}")
    private String imagesPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /* '/js/**'로 호출하는 자원은 '/static/js/' 폴더 아래에서 찾는다. */
        registry.addResourceHandler("/semantic/**")
                .addResourceLocations("classpath:/static/semantic/")
                .setCachePeriod(60 * 60 * 24 * 365);

        /* '/css/**'로 호출하는 자원은 '/static/css/' 폴더 아래에서 찾는다. */
        registry.addResourceHandler("/semantic/**")
                .addResourceLocations("classpath:/static/semantic/")
                .setCachePeriod(60 * 60 * 24 * 365);

        /* '/images/**'로 호출하는 자원은 '/static/img/' 폴더 아래에서 찾는다. */
        registry.addResourceHandler(imagesPath)
                .addResourceLocations("file:///" + resourcePath)
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
                //.addResourceLocations("classpath:/static/img/")

        /*
        '/font/**'로 호출하는 자원은 '/static/font/' 폴더 아래에서 찾는다.
        registry.addResourceHandler("/font/**").addResourceLocations("classpath:/static/font/").setCachePeriod(60 * 60 * 24 * 365);
        */
    }
}
