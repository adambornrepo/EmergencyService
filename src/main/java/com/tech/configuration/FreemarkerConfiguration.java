package com.tech.configuration;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.springframework.context.annotation.Bean;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import java.io.IOException;

@org.springframework.context.annotation.Configuration
public class FreemarkerConfiguration {

    @Bean
    public FreeMarkerConfigurationFactoryBean freemarkerConfig() {
        FreeMarkerConfigurationFactoryBean configFactoryBean = new FreeMarkerConfigurationFactoryBean();
        configFactoryBean.setTemplateLoaderPath("classpath:/templates");
        configFactoryBean.setDefaultEncoding("UTF-8");
        return configFactoryBean;
    }

    @Bean
    public Configuration configuration() throws TemplateException, IOException {
        return freemarkerConfig().createConfiguration();
    }


}
