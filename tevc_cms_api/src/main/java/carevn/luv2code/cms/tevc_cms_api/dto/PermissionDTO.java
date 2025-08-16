package carevn.luv2code.cms.tevc_cms_api.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class PermissionDTO {
    private UUID id;

    private String resource;

    private String action;

    private String description;
}
