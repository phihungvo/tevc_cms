package carevn.luv2code.cms.tevc_cms_api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "timesheets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Timesheet {
    @Id
    @GeneratedValue
    UUID id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    Employee employee;

    @ManyToOne
    @JoinColumn(name = "project_id")
    Project project;

    Date date;

    Double hoursWorked;

    String description;

    String status; // PENDING, APPROVED, REJECTED

    @ManyToOne
    @JoinColumn(name = "approver_id")
    Employee approver;

    String comments;

    Date submissionDate;

    Date approvalDate;
}
