package com.example.demo.config;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ClasspathLoader;
import com.mitchellbosecke.pebble.loader.Loader;
import com.mitchellbosecke.pebble.spring4.PebbleViewResolver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
@ComponentScan({"com.example.demo"})
public class SpringWebConfig implements WebMvcConfigurer {

    @Bean
    public Loader<?> pebbleLoader() {
        ClasspathLoader loader = new ClasspathLoader();
        loader.setPrefix("templates/pebble/");
        loader.setSuffix(".html");
        return loader;
    }

    @Bean
    public PebbleEngine pebbleEngine() {
        // loader 에서 설정해주고 .loader 로 가져올수 있는것 같음.
        return new PebbleEngine.Builder()
            .loader(pebbleLoader())
            .cacheActive(false)
            .build();
    }

    @Bean
    public ViewResolver viewResolver() {
        PebbleViewResolver resolver = new PebbleViewResolver();
        resolver.setPrefix("templates/pebble/");
        resolver.setSuffix(".html");
        resolver.setContentType("text/html; charset=UTF-8");
        resolver.setPebbleEngine(pebbleEngine());
        return resolver;
    }
}
