package com.dpdc.realestate.configs;


import com.dpdc.realestate.jwt.JwtAuthenticationEntryPoint;
import com.dpdc.realestate.jwt.JwtRequestFilter;
import com.dpdc.realestate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableTransactionManagement
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests()
                // Property
                .antMatchers(HttpMethod.POST, "/api/properties/").hasRole("CUSTOMER")
                .antMatchers(HttpMethod.GET, "/api/properties/my-property/{customerId}/").hasRole("CUSTOMER") // id là id customer
                .antMatchers(HttpMethod.DELETE, "/api/properties/hard/{propertyId}/").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/properties/assign/{propertyId}/").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/properties/{propertyId}/").hasAnyRole("CUSTOMER", "ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/properties/{propertyId}/").hasAnyRole("CUSTOMER", "ADMIN")
                .antMatchers(HttpMethod.PATCH, "/api/properties/undeleted/{propertyId}/").hasAnyRole("CUSTOMER", "ADMIN")
                .antMatchers(HttpMethod.PATCH, "/api/properties/active/{propertyId}/").hasAnyRole("ADMIN", "STAFF")
                // Comments
                .antMatchers(HttpMethod.POST, "/api/comments/{propertyId}/").hasRole("CUSTOMER")
                .antMatchers(HttpMethod.DELETE, "/api/comments/{commentId}/").hasAnyRole("CUSTOMER", "ADMIN")
                // Reviews
                .antMatchers(HttpMethod.POST, "/api/reviews/{staffId}/").hasRole("CUSTOMER")
                .antMatchers(HttpMethod.GET, "/api/reviews/{staffId}/").hasAnyRole("ADMIN", "STAFF")
                .antMatchers(HttpMethod.DELETE, "/api/reviews/{staffId}/").hasRole("ADMIN")
                // Packages
                .antMatchers(HttpMethod.POST, "/api/packages/").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/packages/").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/packages/").hasAnyRole("CUSTOMER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/packages/{packageId}/").hasRole("ADMIN")
                // Register Packages
                .antMatchers(HttpMethod.POST, "/api/register-package/bill/{customerId}/").hasRole("CUSTOMER")
                // Payment
                .antMatchers(HttpMethod.POST, "/api/payment/").hasRole("CUSTOMER")
                .antMatchers(HttpMethod.GET, "/api/payment/{customerId}/").hasAnyRole("CUSTOMER", "ADMIN")
                // Blog
                .antMatchers(HttpMethod.POST, "/api/blogs/").hasAnyRole("STAFF", "ADMIN")
                .antMatchers(HttpMethod.PATCH, "/api/blogs/publish/{blogId}/").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/blogs/{blogId}/").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/blogs/{blogId}/").hasRole("ADMIN")

                .anyRequest().permitAll() // All other endpoints are open to all users
                .and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }




    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
