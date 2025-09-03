package carevn.luv2code.cms.tevc_cms_api.entity.model;

import java.util.List;

import carevn.luv2code.cms.tevc_cms_api.enums.UserStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Represents a user domain object as {@link UserModel} in the application.
 */
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class UserModel extends BaseDomainModel {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private UserStatus userStatus;
    private List<Role> roles;
    private List<Permission> permissions;
}
