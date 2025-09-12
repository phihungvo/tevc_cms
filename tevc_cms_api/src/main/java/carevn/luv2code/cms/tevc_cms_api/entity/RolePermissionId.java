package carevn.luv2code.cms.tevc_cms_api.entity;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class RolePermissionId implements Serializable {
    private Integer roleId;
    private Integer permissionId;
}
