package carevn.luv2code.cms.tevc_cms_api.dto;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class RoleDTO {
    private UUID id;

    private String name;

    private String description;

    private Set<UUID> permissions; 
}
