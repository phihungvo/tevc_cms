package carevn.luv2code.cms.tevc_cms_api.dto;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.Data;

@Data
public class UserDTO {
    private Integer id;

    private String username;

    private String email;

    private String fullName;

    private String phoneNumber;

    private Boolean enabled;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Set<RoleDTO> roles;
}
