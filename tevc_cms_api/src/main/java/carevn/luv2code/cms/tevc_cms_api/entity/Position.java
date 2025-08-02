package carevn.luv2code.cms.tevc_cms_api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "positions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Position {
    @Id
    @GeneratedValue
    UUID id;

    @Column(nullable = false)
    String title;

    String description;

    @Column(name = "base_salary", nullable = false)
    Double baseSalary;

    @OneToMany(mappedBy = "position")
    Set<Employee> employees;
}
