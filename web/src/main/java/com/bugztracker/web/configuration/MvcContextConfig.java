package com.bugztracker.web.configuration;

import org.joda.time.Period;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

/**
 * Author: Yuliia Vovk
 * Date: 21.02.16
 * Time: 10:10
 */
@EnableWebMvc
@Configuration
@EnableWebSocket
@ComponentScan("com.bugztracker.web.controllers")
@Import({ WebSocketConfig.class })
public class MvcContextConfig extends WebMvcConfigurerAdapter {

    @Bean(name = "webProperties")
    public static PropertySourcesPlaceholderConfigurer initPropertiesConfig() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setPlaceholderPrefix("${web.");
        configurer.setLocations(new ClassPathResource("web.properties"));
        configurer.setIgnoreUnresolvablePlaceholders(true);
        return configurer;
    }

    @Bean
    public InternalResourceViewResolver provideViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/pages/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").
                addResourceLocations("/resources/").
                setCachePeriod(Period.years(1).getSeconds());
    }

    @Bean
    public ServletServerContainerFactoryBean provideServletFactoryBean() {
        ServletServerContainerFactoryBean bean = new ServletServerContainerFactoryBean();
        bean.setMaxBinaryMessageBufferSize(8192);
        bean.setMaxTextMessageBufferSize(8192);
        return bean;
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver cmr = new CommonsMultipartResolver();
        cmr.setDefaultEncoding("utf-8");
        cmr.setMaxUploadSize(500000);
        return cmr;
    }

}
