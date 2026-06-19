package com.ead.notification.configs.security;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class WebSecurityConfig {

    final AuthenticationEntryPointImpl authenticationEntryPoint;
    final JwtProvider jwtProvider;
    final AccessDeniedHandlerImpl accessDeniedHandler;

    public WebSecurityConfig(AuthenticationEntryPointImpl authenticationEntryPoint, JwtProvider jwtProvider, AccessDeniedHandlerImpl accessDeniedHandler) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.jwtProvider = jwtProvider;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    public AuthenticationJwtFilter  authenticationJwtFilter() {
        return new AuthenticationJwtFilter(jwtProvider);
    }

    @Bean
    public RoleHierarchy  roleHierarchy() {
        String hierarchy = "ROLE_ADMIN > ROLE_INSTRUCTOR \n ROLE_INSTRUCTOR > ROLE_STUDENT" +
                "ROLE_STUDENT > ROLE_USER";
        return RoleHierarchyImpl.fromHierarchy(hierarchy);
    }

    @Bean
    public DefaultMethodSecurityExpressionHandler expressionHandler() {
        var expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy());
        return expressionHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .exceptionHandling((excpetion)-> excpetion
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .authorizeHttpRequests(auth  -> auth
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()

                        //.requestMatchers(HttpMethod.GET, "/users/**").hasRole("ADMIN")
                        //.requestMatchers(HttpMethod.GET, "/users/**").hasAnyAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated())
                //.httpBasic(basic -> basic.authenticationEntryPoint(authenticationEntryPoint))
                .formLogin(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        http.addFilterBefore(authenticationJwtFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

/*    @Bean
    public UserDetailsService  userDetailsService() {
        UserDetails userDetails = User.withUsername("admin")
                .password(passwordEncoder().encode("123456"))
                .roles("ADMIN")
                .build();
        System.out.println(userDetails.getPassword());
        return new InMemoryUserDetailsManager(userDetails);
    }*/

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
