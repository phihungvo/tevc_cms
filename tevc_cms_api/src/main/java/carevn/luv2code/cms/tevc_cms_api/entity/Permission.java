package carevn.luv2code.cms.tevc_cms_api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "permissions")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permission {

    @Id
    @GeneratedValue
    UUID id;

    @Column(nullable = false)
    String resource;

    @Column(unique = false)
    String action;

    @Column(nullable = true)
    String description;

    @ManyToMany(mappedBy = "permissions")
    Set<Role> roles;

    @ManyToMany(mappedBy = "permissions")
    Set<User> users;
}
