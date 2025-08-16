package carevn.luv2code.cms.tevc_cms_api.dto.requests;

import java.util.Set;

import carevn.luv2code.cms.tevc_cms_api.enums.Role;
import lombok.Data;

@Data
public class RegisterRequest {

    private String username;

    private String email;

    private String password;

    private Set<Role> roles;
}
