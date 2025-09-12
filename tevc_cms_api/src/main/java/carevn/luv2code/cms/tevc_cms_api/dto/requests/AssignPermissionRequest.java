package carevn.luv2code.cms.tevc_cms_api.dto.requests;

import java.util.Set;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignPermissionRequest {
    @NotNull(message = "Role ID is required")
    private Integer roleId;

    @NotEmpty(message = "Permission IDs are required")
    private Set<Integer> permissionIds;
}
