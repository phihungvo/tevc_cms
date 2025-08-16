package carevn.luv2code.cms.tevc_cms_api.entity;

import java.util.Date;
import java.util.UUID;

import carevn.luv2code.cms.tevc_cms_api.enums.LeaveStatus;
import carevn.luv2code.cms.tevc_cms_api.enums.LeaveType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "leaves")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Leave {
    @Id
    @GeneratedValue
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @ToString.Exclude
    Employee employee;

    Date startDate;
    Date endDate;

    @Enumerated(EnumType.STRING)
    LeaveType leaveType;

    String reason;

    @Enumerated(EnumType.STRING)
    LeaveStatus status;

    String approverComments;
}
