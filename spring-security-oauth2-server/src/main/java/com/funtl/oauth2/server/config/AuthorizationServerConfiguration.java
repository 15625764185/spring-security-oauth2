package com.funtl.oauth2.server.config;

import com.funtl.oauth2.server.config.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;

    @Autowired
    @Qualifier("jwtTokenStore")
    private TokenStore tokenStore;

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    /**
     * 刷新令牌需要使用自定义的授权服务
     */
    @Autowired
    @Qualifier("userDetailsServiceImpl")
    private UserDetailsService userDetailsService;



    /**
     * JDBC令牌存储
     * 访问形式: xxx/xxx?access_token=xxxx
     * @return
     */
//    @Bean
//    public TokenStore tokenStore() {
//        // 基于 JDBC 实现，令牌保存到数据
//        return new JdbcTokenStore(dataSource);
//    }

    @Bean
    public ClientDetailsService jdbcClientDetails() {
        // 基于 JDBC 实现，需要事先在数据库配置客户端信息
        return new JdbcClientDetailsService(dataSource);
    }

    /**
     * 配置token的存储和时间等属性
     * @return
     */
//    @Bean
//    public DefaultTokenServices tokenServices(){
//        DefaultTokenServices tokenServices = new DefaultTokenServices();
//        tokenServices.setAccessTokenValiditySeconds(20);//设置20秒过期
//        tokenServices.setRefreshTokenValiditySeconds(666);//设置刷新token的过期时间
//        tokenServices.setTokenStore(tokenStore);
//        return tokenServices;
//    }



    /**
     * http://localhost:8080/oauth/token
     * 参数：
     * grant_type : authorization_code,password
     * code:  授权密码
     * 或者:
     * username:xx
     * password:xx
     *
     * 并且要加上Authorization的用户名密码就是
     * client_id 和  client_secret
     * @param endpoints 授权服务器端点令牌配置
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 设置令牌
        endpoints
                .authenticationManager(authenticationManager)
                //刷新token需要配置userDetailsService重新加载权限,需要在authenticationManager之后配置
                .userDetailsService(userDetailsService)
                 .tokenStore(tokenStore)
                .accessTokenConverter(jwtAccessTokenConverter);
               // .tokenServices(tokenServices());

    }


    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                //exposes public key for token verification if using JWT tokens
                .tokenKeyAccess("permitAll()")
                //used by Resource Servers to decode access tokens
                .checkTokenAccess("isAuthenticated()")
                //allowFormAuthenticationForClients是为了注册clientCredentialsTokenEndpointFilter
                //clientCredentialsTokenEndpointFilter,解析request中的client_id和client_secret
                //构造成UsernamePasswordAuthenticationToken,然后通过UserDetailsService查询作简单的认证而已
                //一般是针对password模式和client_credentials
                //当然也可以使用http basic认证
                //如果使用了http basic认证,就不用使用clientCredentialsTokenEndpointFilter
                //因为本质是一样的
                .allowFormAuthenticationForClients();
    }

    /**
     * 客户端配置
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 读取客户端配置
        clients.withClientDetails(jdbcClientDetails());
    }
}
