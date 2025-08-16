package carevn.luv2code.cms.tevc_cms_api.entity;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "contracts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Contract {
    @Id
    @GeneratedValue
    UUID id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    Employee employee;

    @Column(nullable = false)
    String contractType; // FULL_TIME, PART_TIME, TEMPORARY

    Date startDate;

    Date endDate;

    Double basicSalary;

    String position;

    String terms;

    String status; // ACTIVE, TERMINATED, EXPIRED

    Date signedDate;

    @Column(name = "probation_period")
    Integer probationPeriod;

    String terminationReason;

    Date terminationDate;
}
