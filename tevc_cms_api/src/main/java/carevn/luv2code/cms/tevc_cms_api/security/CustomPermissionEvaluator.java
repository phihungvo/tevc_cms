package carevn.luv2code.cms.tevc_cms_api.security;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import carevn.luv2code.cms.tevc_cms_api.entity.ApiPermission;
import carevn.luv2code.cms.tevc_cms_api.repository.ApiPermissionRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final ApiPermissionRepository apiPermissionRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetUrl, Object httpMethod) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        // Ép kiểu authorities thành Collection<GrantedAuthority>
        @SuppressWarnings("unchecked")
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) authentication.getAuthorities();

        // Tìm permission cần thiết cho API từ DB
        Optional<ApiPermission> apiPermission =
                apiPermissionRepository.findByEndpointAndHttpMethod(targetUrl.toString(), httpMethod.toString());

        if (apiPermission.isEmpty()) {
            return false; // Không tìm thấy cấu hình API -> từ chối
        }

        // Kiểm tra xem user có permission cần thiết không
        String requiredPermission = apiPermission.get().getPermission().getName();
        return authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals(requiredPermission));
    }

    @Override
    public boolean hasPermission(
            Authentication authentication, Serializable targetId, String targetType, Object permission) {
        // Không sử dụng trong trường hợp này
        return false;
    }
}
