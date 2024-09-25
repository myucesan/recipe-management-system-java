package recipes.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import recipes.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private UserService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .httpBasic(Customizer.withDefaults())     // Default Basic auth config
//                .csrf(configurer -> configurer.disable()) // for POST requests via Postman
//                .authorizeHttpRequests((requests) -> requests.requestMatchers("/api/register").permitAll())
//                .authorizeHttpRequests((requests) -> requests.requestMatchers("/api/recipe/new").authenticated())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/error").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/api/recipe/new").authenticated()
//                        .requestMatchers(HttpMethod.POST, "/actuator/shutdown").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/test").hasRole("USER")
//                        .anyRequest().denyAll()
//                );

        http
                .httpBasic(Customizer.withDefaults())     // Default Basic auth config
                .csrf(configurer -> configurer.disable()) // for POST requests via Postman
                .authorizeHttpRequests((requests) ->  requests
                .requestMatchers("/api/register").permitAll()
                .requestMatchers("/api/recipe/{id}").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/recipe/{id}").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/recipe/new").authenticated()
                .requestMatchers("/api/recipe/search/").authenticated()
                .requestMatchers(HttpMethod.POST, "/actuator/shutdown").permitAll()
                .requestMatchers("/error").permitAll()
                .anyRequest().denyAll()
        );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

