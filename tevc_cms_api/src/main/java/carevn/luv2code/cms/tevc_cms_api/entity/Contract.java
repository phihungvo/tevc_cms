package carevn.luv2code.cms.tevc_cms_api.entity;

import java.time.LocalDate;
import java.util.List;

import carevn.luv2code.cms.tevc_cms_api.enums.ContractStatus;
import carevn.luv2code.cms.tevc_cms_api.enums.ContractType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    Employee employee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    ContractType contractType;

    LocalDate startDate;

    LocalDate endDate;

    Double basicSalary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id", nullable = false)
    private Position position;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    ContractStatus status;

    LocalDate signedDate;

    @Column(name = "probation_period")
    Integer probationPeriod; // thời gian thử việc (tháng)

    String terminationReason; // Lý do chấm dứt hợp đồng

    LocalDate terminationDate; // Ngày chấm dứt hợp đồng

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<File> files;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    User createdBy;
}
