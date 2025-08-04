package carevn.luv2code.cms.tevc_cms_api.dto;

import lombok.Data;
import java.util.Set;
import java.util.UUID;

@Data
public class PermissionDTO {
    private UUID id;

    private String resource;

    private String action;

    private String description;

}
