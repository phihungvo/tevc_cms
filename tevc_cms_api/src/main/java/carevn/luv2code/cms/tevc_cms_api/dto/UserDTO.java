package carevn.luv2code.cms.tevc_cms_api.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {

    UUID id;

    String userName;

    String firstName;

    String lastName;

    String password;

    String email;

    String address;

    String phoneNumber;

    String profilePicture;

    String bio;

    List<String> roles;

    Date createAt;

    Date updateAt;

    boolean enabled;

}
