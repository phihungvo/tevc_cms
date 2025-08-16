package carevn.luv2code.cms.tevc_cms_api.dto;

import java.util.Set;
import java.util.UUID;

import lombok.Data;

@Data
public class RoleDTO {
    private UUID id;

    private String name;

    private String description;

    private Set<UUID> permissions;
}
