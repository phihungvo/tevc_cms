package carevn.luv2code.cms.tevc_cms_api.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import carevn.luv2code.cms.tevc_cms_api.enums.PayrollStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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

    String period; // Kỳ lương (tháng/năm) YYYY-MM

    Double basicSalary; // Lương cơ bản

    Double overtime; // Tổng số giờ làm thêm

    Double bonus; // Tổng thưởng

    Double allowances; // Tổng phụ cấp

    Double deductions; // Tổng khấu trừ

    Double tax; // Tổng thuế

    Double insurance; // Tổng bảo hiểm

    Double netSalary; // Lương thực nhận (net salary)

    @Enumerated(EnumType.STRING)
    PayrollStatus status;

    Date processedDate; // Ngày xử lý lương

    Date paidDate; // Ngày trả lương

    @OneToMany(mappedBy = "payroll", cascade = CascadeType.ALL)
    @ToString.Exclude
    Set<PayrollDetail> details = new HashSet<>();
}
