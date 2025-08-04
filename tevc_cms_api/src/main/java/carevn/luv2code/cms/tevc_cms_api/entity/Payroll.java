package carevn.luv2code.cms.tevc_cms_api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "payrolls")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payroll {
    @Id
    @GeneratedValue
    UUID id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    Employee employee;

    String period; // YYYY-MM

    Double basicSalary;

    Double overtime;

    Double bonus;

    Double allowances;

    Double deductions;

    Double tax;

    Double insurance;

    Double netSalary;

    String status; // PENDING, PROCESSED, PAID

    Date processedDate;

    Date paidDate;

    @OneToMany(mappedBy = "payroll", cascade = CascadeType.ALL)
    @ToString.Exclude
    Set<PayrollDetail> details = new HashSet<>();
}
