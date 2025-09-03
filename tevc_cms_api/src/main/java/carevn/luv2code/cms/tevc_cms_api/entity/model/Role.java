package carevn.luv2code.cms.tevc_cms_api.entity.model;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Represents a role domain object as {@link Role} in the application.
 */
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseDomainModel {

    private String id;
    private String name;
    private List<Permission> permissions;
}
