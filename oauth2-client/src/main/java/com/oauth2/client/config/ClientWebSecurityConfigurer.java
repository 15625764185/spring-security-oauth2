package com.oauth2.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(101)
public class ClientWebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Value("${oauth2-logout-url}")
    private   String LOGOUT_URL;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors().disable()
                .logout()
                .logoutSuccessUrl(LOGOUT_URL)
                .and().authorizeRequests().anyRequest().authenticated();;
    }
}