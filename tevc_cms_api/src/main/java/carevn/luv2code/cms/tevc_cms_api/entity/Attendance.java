package carevn.luv2code.cms.tevc_cms_api.entity;

import carevn.luv2code.cms.tevc_cms_api.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "attendances")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Attendance {
    @Id
    @GeneratedValue
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @ToString.Exclude
    Employee employee;

    Date checkIn;

    Date checkOut;

    @Enumerated(EnumType.STRING)
    AttendanceStatus status;

    String notes;
}
