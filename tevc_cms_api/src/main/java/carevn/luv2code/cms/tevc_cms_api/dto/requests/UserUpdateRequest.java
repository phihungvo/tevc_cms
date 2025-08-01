package carevn.luv2code.cms.tevc_cms_api.dto.requests;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phoneNumber;
    private String profilePicture;
    private String bio;
    private boolean enabled;
    private List<String> roles;
    private List<String> permissions; // Thêm danh sách quyền
}