package carevn.luv2code.cms.tevc_cms_api.entity;

import java.util.Date;

import carevn.luv2code.cms.tevc_cms_api.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "attendances")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @ToString.Exclude
    Employee employee;

    @Column(name = "attendance_date", nullable = false)
    Date attendanceDate;

    Date checkIn;

    Date checkOut;

    @Enumerated(EnumType.STRING)
    AttendanceStatus status;

    @Column(name = "work_hours")
    Double workHours; // số giờ làm thực tế (có thể tính từ checkIn/checkOut)

    String notes;
}
