package carevn.luv2code.cms.tevc_cms_api.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import carevn.luv2code.cms.tevc_cms_api.entity.ApiPermission;
import carevn.luv2code.cms.tevc_cms_api.repository.ApiPermissionRepository;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomBearerTokenAuthenticationFilter customBearerTokenAuthenticationFilter;
    private final ApiPermissionRepository apiPermissionRepository;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource())) // Add CORS configuration
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(customAuthenticationEntryPoint))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.OPTIONS, "/**")
                        .permitAll() // Allow OPTIONS requests
                        .requestMatchers("/api/auth/**")
                        .permitAll()
                        .anyRequest()
                        .access(dynamicAuthorizationManager()))
                .addFilterBefore(customBearerTokenAuthenticationFilter, AuthorizationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // Adjust to your frontend URL
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthorizationManager<RequestAuthorizationContext> dynamicAuthorizationManager() {
        return (authentication, context) -> {
            String method = context.getRequest().getMethod();
            String uri = context.getRequest().getRequestURI();

            List<ApiPermission> apiPermissions = apiPermissionRepository.findAll();

            for (ApiPermission apiPermission : apiPermissions) {
                AntPathRequestMatcher matcher =
                        new AntPathRequestMatcher(apiPermission.getEndpoint(), apiPermission.getHttpMethod());

                if (matcher.matches(context.getRequest())) {
                    boolean hasPermission = authentication.get().getAuthorities().stream()
                            .anyMatch(granted -> granted.getAuthority()
                                    .equals(apiPermission.getPermission().getName()));

                    return new AuthorizationDecision(hasPermission);
                }
            }

            return new AuthorizationDecision(false);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
