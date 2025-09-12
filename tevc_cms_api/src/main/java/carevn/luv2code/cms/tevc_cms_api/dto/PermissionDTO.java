package carevn.luv2code.cms.tevc_cms_api.dto;

import java.time.LocalDateTime;

import carevn.luv2code.cms.tevc_cms_api.enums.HttpMethod;
import lombok.Data;

@Data
public class PermissionDTO {
    private Integer id;
    private String name;
    private String description;
    private String apiEndpoint;
    private HttpMethod httpMethod;
    private String resourcePattern;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
