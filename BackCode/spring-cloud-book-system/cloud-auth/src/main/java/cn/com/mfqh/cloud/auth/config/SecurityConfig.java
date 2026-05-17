package cn.com.mfqh.cloud.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;

import java.util.UUID;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. 授权服务器过滤器链（官方标准配置，优先级最高）
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
        http.apply(authorizationServerConfigurer);

        // 放行授权服务器端点
        http.authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated()
        );

        // 启用表单登录（OAuth2 必须）
        http.formLogin(form -> form.permitAll());

        return http.build();
    }

    // 2. 应用安全过滤器链（登录页配置）
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                );

        return http.build();
    }

    // 3. 注册客户端（service-user）
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient client = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("service-user")
                .clientSecret("{noop}user-secret-123")
                .authorizationGrantType(org.springframework.security.oauth2.core.AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(org.springframework.security.oauth2.core.AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://localhost:10302/callback")
                .clientAuthenticationMethod(org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .scope("read")
                .build();

        return new InMemoryRegisteredClientRepository(client);
    }

    // 4. 测试用户（admin / 123456）
    @Bean
    public UserDetailsService userDetailsService() {
        // 直接创建用户，确保密码编码器匹配
        UserDetails user = User.withUsername("admin")
                .password("{noop}123456") // 关键：必须加 {noop}，表示明文密码
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    // 5. 密码编码器（必须）
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}