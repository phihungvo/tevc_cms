package carevn.luv2code.cms.tevc_cms_api.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {

    String userName;

    String firstName;

    String lastName;

    String email;

    String password;

    String address;

    String phoneNumber;

    String profilePicture;

    String bio;

    boolean enabled;

    List<String> roles;

    List<String> permissions;
}