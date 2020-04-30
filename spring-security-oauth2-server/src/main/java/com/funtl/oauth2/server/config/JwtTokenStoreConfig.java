package com.funtl.oauth2.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * @author ：xiao7
 * @date ：Created in 2020/4/29 20:38
 * @description：配置jwt令牌存储
 */
@Configuration
public class JwtTokenStoreConfig {

    /**
     * jwt令牌访问
     *
     * 形式: Authorization :  bearer xxxxxx
     * @return
     */
    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }




    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        // 配置jwt使用的密钥
        jwtAccessTokenConverter.setSigningKey("xiao7");
        return jwtAccessTokenConverter;
    }

}
