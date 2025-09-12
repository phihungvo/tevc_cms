package carevn.luv2code.cms.tevc_cms_api.dto.requests;

import java.util.Set;

import lombok.Data;

@Data
public class UpdateRoleRequest {
    private String description;
    private Set<Integer> permissionIds;
}
