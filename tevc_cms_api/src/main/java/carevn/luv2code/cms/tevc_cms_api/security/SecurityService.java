package carevn.luv2code.cms.tevc_cms_api.security;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import carevn.luv2code.cms.tevc_cms_api.entity.Permission;
import carevn.luv2code.cms.tevc_cms_api.entity.User;
import carevn.luv2code.cms.tevc_cms_api.repository.ApiPermissionRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service("securityService")
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;
    private final ApiPermissionRepository apiPermissionRepository;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Cacheable(value = "userPermissions", key = "#username")
    public Set<String> getUserPermissions(String username) {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getName)
                .filter(name -> name != null)
                .collect(Collectors.toSet());
    }

    public boolean hasPermissionForRequest() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }

        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String requestUri = request.getRequestURI();
        String httpMethod = request.getMethod();

        Set<String> requiredPermissions = apiPermissionRepository.findAll().stream()
                .filter(apiPermission -> pathMatcher.match(apiPermission.getEndpoint(), requestUri)
                        && apiPermission.getHttpMethod().equalsIgnoreCase(httpMethod))
                .map(apiPermission -> apiPermission.getPermission().getName())
                .collect(Collectors.toSet());

        Set<String> userPermissions = getUserPermissions(auth.getName());
        return requiredPermissions.stream().anyMatch(userPermissions::contains);
    }
}
