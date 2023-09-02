package com.dpdc.realestate.configs;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class CloudinaryConfig {
    @Autowired
    private Environment env;
    @Bean
    public Cloudinary cloudinary() {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", env.getProperty("cloudinary.cloud_name"),
                "api_key", env.getProperty("cloudinary.api_key"),
                "api_secret", env.getProperty("cloudinary.secret_key"),
                "secure", true));
        return cloudinary;
    }
}
