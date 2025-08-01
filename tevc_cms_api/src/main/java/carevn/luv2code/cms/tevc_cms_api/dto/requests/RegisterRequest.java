package carevn.luv2code.cms.tevc_cms_api.dto.requests;

import carevn.luv2code.cms.tevc_cms_api.enums.Role;
import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequest {

    private String username;

    private String email;

    private String password;

    private Set<Role> roles;

}
