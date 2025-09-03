package carevn.luv2code.cms.tevc_cms_api.security;

import org.springframework.security.access.ConfigAttribute;

public class SecurityConfigAttribute implements ConfigAttribute {
    private final String permission;

    public SecurityConfigAttribute(String permission) {
        this.permission = permission;
    }

    @Override
    public String getAttribute() {
        return permission;
    }
}
