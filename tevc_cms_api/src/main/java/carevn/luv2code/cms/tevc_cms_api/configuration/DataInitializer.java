package carevn.luv2code.cms.tevc_cms_api.configuration;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import carevn.luv2code.cms.tevc_cms_api.entity.Permission;
import carevn.luv2code.cms.tevc_cms_api.entity.Role;
import carevn.luv2code.cms.tevc_cms_api.entity.User;
import carevn.luv2code.cms.tevc_cms_api.enums.HttpMethod;
import carevn.luv2code.cms.tevc_cms_api.repository.PermissionRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.RoleRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            log.info("Initializing default data...");
            initializeDefaultData();
            log.info("Default data initialized successfully!");
        }
    }

    private void initializeDefaultData() {
        // Tạo Permissions
        Permission userReadPermission =
                createPermission("USER_READ", "Read user profile", "/api/user/**", HttpMethod.GET);
        Permission userWritePermission =
                createPermission("USER_WRITE", "Update user profile", "/api/user/**", HttpMethod.PUT);

        Permission adminReadPermission =
                createPermission("ADMIN_READ", "Admin read access", "/api/admin/**", HttpMethod.GET);
        Permission adminWritePermission =
                createPermission("ADMIN_WRITE", "Admin write access", "/api/admin/**", HttpMethod.POST);
        Permission adminUpdatePermission =
                createPermission("ADMIN_UPDATE", "Admin update access", "/api/admin/**", HttpMethod.PUT);
        Permission adminDeletePermission =
                createPermission("ADMIN_DELETE", "Admin delete access", "/api/admin/**", HttpMethod.DELETE);

        Permission managerReadPermission =
                createPermission("MANAGER_READ", "Manager read access", "/api/manager/**", HttpMethod.GET);
        Permission managerWritePermission =
                createPermission("MANAGER_WRITE", "Manager write access", "/api/manager/**", HttpMethod.POST);
        Permission managerUpdatePermission =
                createPermission("MANAGER_UPDATE", "Manager update access", "/api/manager/**", HttpMethod.PUT);

        Permission hrReadPermission = createPermission("HR_READ", "HR read access", "/api/hr/**", HttpMethod.GET);
        Permission hrWritePermission = createPermission("HR_WRITE", "HR write access", "/api/hr/**", HttpMethod.POST);
        Permission hrDeletePermission =
                createPermission("HR_DELETE", "HR delete access", "/api/hr/**", HttpMethod.DELETE);

        // Tạo Roles
        Role userRole = createRole("USER", "Basic user role");
        userRole.setPermissions(Set.of(userReadPermission, userWritePermission));

        Role adminRole = createRole("ADMIN", "Administrator role");
        adminRole.setPermissions(Set.of(
                userReadPermission,
                userWritePermission,
                adminReadPermission,
                adminWritePermission,
                adminUpdatePermission,
                adminDeletePermission,
                managerReadPermission,
                managerWritePermission,
                managerUpdatePermission,
                hrReadPermission,
                hrWritePermission,
                hrDeletePermission));

        Role managerRole = createRole("MANAGER", "Manager role");
        managerRole.setPermissions(Set.of(
                userReadPermission,
                userWritePermission,
                managerReadPermission,
                managerWritePermission,
                managerUpdatePermission));

        Role hrRole = createRole("HR", "Human Resources role");
        hrRole.setPermissions(Set.of(userReadPermission, hrReadPermission, hrWritePermission, hrDeletePermission));

        // Save roles
        roleRepository.save(userRole);
        roleRepository.save(adminRole);
        roleRepository.save(managerRole);
        roleRepository.save(hrRole);

        // Tạo Users
        User adminUser = createUser("admin", "admin123", "admin@example.com", "System Administrator", true);
        adminUser.setRoles(Set.of(adminRole));

        User managerUser = createUser("manager", "manager123", "manager@example.com", "Department Manager", true);
        managerUser.setRoles(Set.of(managerRole));

        User hrUser = createUser("hr", "hr123", "hr@example.com", "HR Specialist", true);
        hrUser.setRoles(Set.of(hrRole));

        User regularUser = createUser("user", "user123", "user@example.com", "Regular User", true);
        regularUser.setRoles(Set.of(userRole));

        // Save users
        userRepository.save(adminUser);
        userRepository.save(managerUser);
        userRepository.save(hrUser);
        userRepository.save(regularUser);

        log.info("Created default users:");
        log.info("Admin: admin/admin123");
        log.info("Manager: manager/manager123");
        log.info("HR: hr/hr123");
        log.info("User: user/user123");
    }

    private Permission createPermission(String name, String description, String apiEndpoint, HttpMethod httpMethod) {
        Permission permission = new Permission();
        permission.setName(name);
        permission.setDescription(description);
        permission.setApiEndpoint(apiEndpoint);
        permission.setHttpMethod(httpMethod);
        return permissionRepository.save(permission);
    }

    private Role createRole(String name, String description) {
        Role role = new Role();
        role.setName(name);
        role.setDescription(description);
        return role; // Will be saved later with permissions
    }

    private User createUser(String username, String password, String email, String fullName, boolean enabled) {
        User user = new User();
        user.setUserName(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        //        user.setFullName(fullName);
        user.setEnabled(enabled);
        return user; // Will be saved later with roles
    }
}
