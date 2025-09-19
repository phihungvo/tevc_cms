package carevn.luv2code.cms.tevc_cms_api.dto;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleDTO {
    Integer id;

    String name;

    String description;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

    Set<PermissionDTO> permissions;
}
