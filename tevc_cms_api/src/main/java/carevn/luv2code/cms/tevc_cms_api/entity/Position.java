package carevn.luv2code.cms.tevc_cms_api.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import carevn.luv2code.cms.tevc_cms_api.enums.PositionType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "positions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false)
    String title;

    String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "position_type", nullable = false)
    PositionType positionType;

    @Column(name = "base_salary", nullable = false)
    Double baseSalary;

    @JsonManagedReference
    @OneToMany(mappedBy = "position", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    Set<Employee> employees;
}
