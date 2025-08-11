package carevn.luv2code.cms.tevc_cms_api.entity;

import carevn.luv2code.cms.tevc_cms_api.enums.PositionType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "position_type", nullable = false)
    PositionType positionType;

    @Column(name = "base_salary", nullable = false)
    Double baseSalary;

//    @JsonManagedReference
//    @OneToMany(mappedBy = "position", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    Set<Employee> employees;

}
