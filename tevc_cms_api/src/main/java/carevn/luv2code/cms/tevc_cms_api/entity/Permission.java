package carevn.luv2code.cms.tevc_cms_api.entity;

import java.text.Normalizer;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String resource;

    @Column(unique = false)
    String action;

    @Column(nullable = true)
    String description;

    @PrePersist
    @PreUpdate
    private void setName() {
        this.resource = normalizeString(this.resource);
        this.action = normalizeString(this.action);
        this.name = this.resource + ":" + this.action;
    }

    private String normalizeString(String input) {
        if (input == null) {
            return "";
        }
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("[^a-zA-Z0-9]", "")
                .toUpperCase();
    }
}
