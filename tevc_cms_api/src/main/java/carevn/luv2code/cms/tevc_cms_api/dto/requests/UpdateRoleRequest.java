package carevn.luv2code.cms.tevc_cms_api.dto.requests;

import java.util.Set;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateRoleRequest {
    String name;

    String description;

    Set<Integer> permissionIds;
}
