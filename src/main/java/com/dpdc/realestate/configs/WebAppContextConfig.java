package com.dpdc.realestate.configs;

import com.dpdc.realestate.formatter.CategoryFormatter;
import com.dpdc.realestate.formatter.CustomerFormatter;
import com.dpdc.realestate.formatter.PropertyFormatter;
import com.dpdc.realestate.formatter.UserFormatter;
import com.dpdc.realestate.models.entity.Property;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan(basePackages = {
        "com.dpdc"
})
public class WebAppContextConfig implements WebMvcConfigurer {
    @Bean
    public InternalResourceViewResolver defaultViewResolver() {
        return new InternalResourceViewResolver();
    }




    @Primary
    @Bean
    public FreeMarkerConfigurationFactoryBean factoryBean() {
        FreeMarkerConfigurationFactoryBean bean=new FreeMarkerConfigurationFactoryBean();
        bean.setTemplateLoaderPath("classpath:/templates");
        return bean;
    }

    @Bean
    public ModelMapper modelMapper () {
        ModelMapper modelMapper = new ModelMapper();

        // Allow ModelMapper to access private fields
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        // Create a custom mapping to ignore 'location' when mapping Property
        modelMapper.createTypeMap(Property.class, Property.class)
                .addMapping(Property::getLocation, Property::setLocation);

        return modelMapper;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {

        registry.addFormatter(new CategoryFormatter());
        registry.addFormatter(new PropertyFormatter());
        registry.addFormatter(new CustomerFormatter());
        registry.addFormatter(new UserFormatter());


    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Đăng ký một đường dẫn URL tùy chỉnh cho tài nguyên tĩnh
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}
