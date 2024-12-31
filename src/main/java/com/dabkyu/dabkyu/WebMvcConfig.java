package com.dabkyu.dabkyu;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/product/images/**")
				.addResourceLocations("file:///c:/Repository/dabkyu/product/images/");
        registry.addResourceHandler("/product/thumbnails/**")
				.addResourceLocations("file:///c:/Repository/dabkyu/product/thumbnails/");
        registry.addResourceHandler("/question/images/**")
				.addResourceLocations("file:///c:/Repository/dabkyu/question/images/");
    }
}
