package carevn.luv2code.cms.tevc_cms_api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "salaries")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Salary {
    @Id
    @GeneratedValue
     UUID id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
     Employee employee;

     Date paymentDate;

     Double basicSalary;

     Double bonus;

     Double deductions;

     Double netSalary;

     String paymentStatus;

     String period; // Monthly period: YYYY-MM
}
