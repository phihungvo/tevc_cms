package carevn.luv2code.cms.tevc_cms_api.dto;

import lombok.Data;

@Data
public class PermissionDTO {
    private Integer id;

    private String resource;

    private String action;

    private String description;
}
