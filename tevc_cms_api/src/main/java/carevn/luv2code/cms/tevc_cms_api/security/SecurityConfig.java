package carevn.luv2code.cms.tevc_cms_api.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF since we're using JWT authentication
                .csrf(csrf -> csrf.disable())

                // Set session management to stateless (JWT)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configure authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers(
                                // Auth
                                "/api/auth/**",

                                // Collections
                                "/api/collections/**",
                                "/api/collections/*/collect/*",
                                "/api/collections/*/un_collect/*",

                                // Movie
                                "/api/movie/**",
                                "/api/movie/detail/**",
                                "/api/movie/korea-movie",
                                "/api/movie/korean-movie",
                                "/api/movie/popular",
                                "/api/movie/this-week",
                                "/api/movie/vietnamese-movie",

                                // Trailers
                                "/api/trailers/movie/**",

                                // Storage
                                "/api/storage/files/**",

                                // Comments
                                "/api/comment/getAll/**",
                                "/api/comment/create/**",
                                "/api/comment/movie/**",
                                "/api/comment/{commentId}/reply",

                                //Genres
                                "/api/genres/*/movie",

                                // Actors
                                "/api/actor/findAll"
                        ).permitAll()

                        .requestMatchers(HttpMethod.PUT, "/api/comment/**").permitAll().requestMatchers(HttpMethod.DELETE, "/api/comment/**").permitAll().requestMatchers(HttpMethod.POST, "/api/comment/**").permitAll()

                        // User endpoints
                        .requestMatchers("/api/user/createUser", "/api/user/getAll", "/api/movie/**", "/api/storage/**").hasAnyRole("USER", "ADMIN")

                        // Admin-only endpoints
                        .requestMatchers("/api/admin/**",
                                //"/api/storage/**",
                                "/api/genres/**", "/api/trailers/**", "/api/comment/**", "/api/actor/**").hasRole("ADMIN")

                        // Moderator endpoints
                        .requestMatchers("/api/moderator/**").hasAnyRole("MODERATOR", "ADMIN")

                        // Default: all other requests must be authenticated
                        .anyRequest().authenticated())

                // Configure CORS settings
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(List.of("http://localhost:3000", "https://movienest.click"));
                    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                    configuration.setAllowedHeaders(List.of("*"));
                    configuration.setAllowCredentials(true);
                    return configuration;
                }))

                // Add JWT authentication filter before UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(authProvider);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")  // Thay đổi từ "/api/**" sang "/**"
                        .allowedOrigins("http://localhost:3000", "https://movienest.click").allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS").allowedHeaders("*").allowCredentials(true);
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
