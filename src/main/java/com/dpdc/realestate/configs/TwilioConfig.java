package com.dpdc.realestate.configs;

import com.twilio.Twilio;
import com.twilio.http.TwilioRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class TwilioConfig {
    @Autowired
    private Environment env;

    @Bean
    public TwilioRestClient twilioRestClient() {
        String accountSID = env.getProperty("twilio.account_sid");
        String authToken = env.getProperty("twilio.auth_token");
        Twilio.init(accountSID, authToken);
        return new TwilioRestClient.Builder(accountSID, authToken).build();
    }
}
