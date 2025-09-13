package carevn.luv2code.cms.tevc_cms_api.dto;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.Data;

@Data
public class RoleDTO {
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<PermissionDTO> permissions;
}
