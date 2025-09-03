package carevn.luv2code.cms.tevc_cms_api.entity;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "api_permissions")
public class ApiPermission {
    @Id
    @GeneratedValue
    UUID id;

    @Column(nullable = false)
    String endpoint;

    @Column(nullable = false)
    String httpMethod;

    @ManyToOne
    @JoinColumn(name = "permission_id", nullable = false)
    Permission permission;
}
