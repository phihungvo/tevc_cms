package carevn.luv2code.cms.tevc_cms_api.dto.requests;

import java.util.List;

import lombok.*;

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

    //    private String password;

    private String address;

    private String phoneNumber;

    private String profilePicture;

    private String bio;

    private boolean enabled;

    private List<Integer> roleIds;

    //    private List<UUID> permissions;
}
