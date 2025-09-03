package carevn.luv2code.cms.tevc_cms_api.entity.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

/**
 * Base class named {@link BaseDomainModel} for domain models with common audit fields.
 */
@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseDomainModel {

    @Column(name = "CREATED_AT")
    LocalDateTime createdAt;

    @Column(name = "CREATED_BY")
    String createdBy;

    @Column(name = "UPDATED_AT")
    LocalDateTime updatedAt;

    @Column(name = "UPDATED_BY")
    String updatedBy;
}
