package com.oauth2.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;

/**
 * 认证服务器，用于获取 Token
 * <p>
 * Description:
 * </p>
 *
 * @author Lusifer
 * @version v1.0.0
 * @date 2019-04-01 16:06:45
 * @see com.funtl.oauth2
 */
@EnableOAuth2Sso
@SpringBootApplication
public class OAuth2clientOneApplication {

    public static void main(String[] args) {
        SpringApplication.run(OAuth2clientOneApplication.class, args);
    }

}
