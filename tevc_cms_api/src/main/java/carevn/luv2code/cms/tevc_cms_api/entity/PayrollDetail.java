package carevn.luv2code.cms.tevc_cms_api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "payroll_details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PayrollDetail {
    @Id
    @GeneratedValue
    UUID id;

    @ManyToOne
    @JoinColumn(name = "payroll_id", nullable = false)
    Payroll payroll;

    String type; // BONUS, DEDUCTION, ALLOWANCE

    String description;

    Double amount;

    Date date;

    String category;
}
