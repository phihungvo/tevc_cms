package carevn.luv2code.cms.tevc_cms_api.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {

    UUID id;

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

    List<UUID> roles;

    List<String> roleNames;

    List<UUID> permissions;

    Date createAt;

    Date updateAt;
}
