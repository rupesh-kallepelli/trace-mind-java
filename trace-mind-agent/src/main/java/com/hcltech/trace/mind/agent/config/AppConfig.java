package com.hcltech.trace.mind.agent.config;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableAsync
@EnableWebSecurity
public class AppConfig {

        @Bean
        public WebMvcConfigurer corsConfigurer() {
                return new WebMvcConfigurer() {
                        @Override
                        public void addCorsMappings(CorsRegistry registry) {

                                registry.addMapping("/**").allowedOrigins("*")
                                                .allowedMethods("*");
                        }
                };
        }

        @Bean
        SecurityFilterChain securityFilterChain(
                        HttpSecurity http)
                        throws Exception {

                http
                                .cors(Customizer.withDefaults())
                                .csrf(csrf -> csrf.disable())

                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/actuator/**")
                                                .permitAll()

                                                .requestMatchers(
                                                                "/api/v1/investigations/*/events",
                                                                "/api/v1/investigations/*/running",
                                                                "/api/v1/investigations/*/completed",
                                                                "/api/v1/investigations/*/failed")
                                                .permitAll()

                                                .anyRequest()
                                                .authenticated())

                                .oauth2ResourceServer(oauth -> oauth.authenticationManagerResolver(
                                                authenticationManagerResolver()));

                return http.build();
        }

        @Bean
        AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver() {

                AuthenticationManager google = authenticationManager(
                                "https://accounts.google.com");

                AuthenticationManager microsoft = authenticationManager(
                                "https://login.microsoftonline.com/350e3a44-49a3-4a31-9cd0-67bbdfcd8936/v2.0");

                Map<String, AuthenticationManager> managers = Map.of(

                                "https://accounts.google.com",
                                google,

                                "https://login.microsoftonline.com/350e3a44-49a3-4a31-9cd0-67bbdfcd8936/v2.0",
                                microsoft);

                return new JwtIssuerAuthenticationManagerResolver(
                                managers::get);
        }

        private AuthenticationManager authenticationManager(
                        String issuer) {

                JwtDecoder decoder = JwtDecoders.fromIssuerLocation(
                                issuer);

                JwtAuthenticationProvider provider = new JwtAuthenticationProvider(
                                decoder);

                return provider::authenticate;
        }
}
