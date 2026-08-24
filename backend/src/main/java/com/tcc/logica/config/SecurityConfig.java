package com.tcc.logica.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Temporary configuration: opens up the stateless formula/table endpoints so the
 * truth-table engine can be exercised before Google OAuth2 login is wired up.
 * CSRF is disabled globally for now since there is no session-based, state-changing
 * browser flow yet; revisit once login is implemented.
 *
 * "/error" must stay permitAll: any ResponseStatusException thrown from a controller
 * (not caught by a @RestControllerAdvice) makes Tomcat internally redispatch to
 * /error to render the response, and that redispatch re-enters this filter chain —
 * without this line it gets blocked and the client sees 403 instead of the real
 * status code (400/404/etc).
 */
@Configuration
public class SecurityConfig {

    @Value("${app.cors.allowed-origin:http://localhost:5173}")
    private String allowedOrigin;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/formulas/**", "/api/exercises/**", "/api/me/**", "/api/ranking/**", "/error").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(allowedOrigin));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}
