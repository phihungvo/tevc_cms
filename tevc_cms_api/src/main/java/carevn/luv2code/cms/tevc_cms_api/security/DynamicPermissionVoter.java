package carevn.luv2code.cms.tevc_cms_api.security;

import java.util.Collection;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicPermissionVoter implements AccessDecisionVoter<FilterInvocation> {

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return attribute instanceof SecurityConfigAttribute;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    @Override
    public int vote(Authentication authentication, FilterInvocation fi, Collection<ConfigAttribute> attributes) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ACCESS_DENIED;
        }

        for (ConfigAttribute attribute : attributes) {
            String requiredPermission = attribute.getAttribute();
            boolean hasPermission = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals(requiredPermission));

            if (hasPermission) {
                log.debug("✅ User có quyền {} → ALLOW", requiredPermission);
                return ACCESS_GRANTED;
            } else {
                log.debug("❌ User thiếu quyền {} → DENY", requiredPermission);
            }
        }
        return ACCESS_DENIED;
    }
}
