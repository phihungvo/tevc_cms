package carevn.luv2code.cms.tevc_cms_api.configuration;

import carevn.luv2code.cms.tevc_cms_api.entity.Role;
import carevn.luv2code.cms.tevc_cms_api.entity.User;
import carevn.luv2code.cms.tevc_cms_api.repository.RoleRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    RoleRepository roleRepository;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        String adminEmail = "admin@example.com";
        Role adminRole = roleRepository.findByNameForUpdate("ADMIN")
                .orElseGet(() -> {
                    Role newRole = Role.builder()
                            .id(UUID.randomUUID())
                            .name("ADMIN")
                            .description("Administrator role with full access")
                            .build();
                    return roleRepository.save(newRole);
                });

        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);

        return args -> {
            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                User user = User.builder()
                        .userName("admin1")
                        .email(adminEmail)
                        .password(passwordEncoder.encode("admin_example"))
                        .createAt(new Date())
                        .roles(roles)
                        .enabled(true)
                        .accountNonExpired(true)
                        .credentialsNonExpired(true)
                        .accountNonLocked(true)
                        .build();

                userRepository.save(user);
                log.warn("Admin user has been created with default email: admin@example.com" +
                        " and password: admin_example, please change it!");
            }
        };
    }
}