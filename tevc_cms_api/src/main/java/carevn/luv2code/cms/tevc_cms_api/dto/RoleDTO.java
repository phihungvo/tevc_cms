package carevn.luv2code.cms.tevc_cms_api.dto;

import java.util.Set;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleDTO {
    UUID id;

    String name;

    String description;

    Set<UUID> permissions;

}
