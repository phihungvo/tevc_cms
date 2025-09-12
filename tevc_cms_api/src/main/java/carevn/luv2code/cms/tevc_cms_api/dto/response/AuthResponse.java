package carevn.luv2code.cms.tevc_cms_api.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private String username;
    private List<String> roles;
}
