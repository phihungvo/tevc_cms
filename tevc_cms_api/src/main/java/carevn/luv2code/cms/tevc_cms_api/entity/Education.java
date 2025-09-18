package carevn.luv2code.cms.tevc_cms_api.entity;

import java.time.LocalDate;

import carevn.luv2code.cms.tevc_cms_api.enums.DegreeLevel;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "educations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    Employee employee;

    @Column(nullable = false)
    String institutionName;

    String major;

    @Enumerated(EnumType.STRING)
    DegreeLevel degree;

    LocalDate startDate;

    LocalDate graduationDate;

    Float gpa;
}
