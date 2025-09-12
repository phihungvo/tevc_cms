package carevn.luv2code.cms.tevc_cms_api.dto.requests;

import carevn.luv2code.cms.tevc_cms_api.enums.HttpMethod;
import lombok.Data;

@Data
public class UpdatePermissionRequest {
    private String description;
    private String apiEndpoint;
    private HttpMethod httpMethod;
    private String resourcePattern;
}
