package carevn.luv2code.cms.tevc_cms_api.dto.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionCreateRequest {

    @NotBlank
    String resource;

    @NotBlank
    String action;
}
