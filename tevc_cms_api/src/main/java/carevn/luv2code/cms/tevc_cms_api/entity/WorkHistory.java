package carevn.luv2code.cms.tevc_cms_api.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "work_histories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    Employee employee;

    String companyName;

    String position;

    LocalDate startDate;

    LocalDate endDate;

    String companyAddress;

    String reasonForLeaving;

    Double salary;

    String contractType;

    String supervisorName;
}
