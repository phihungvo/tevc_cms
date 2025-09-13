package carevn.luv2code.cms.tevc_cms_api.entity;

import java.util.Date;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "performances")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Performance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    Employee employee;

    Date reviewDate;

    String reviewPeriod; // YEARLY, QUARTERLY, MONTHLY

    Integer rating; // 1-5

    String comments;

    String goals;

    String improvements;

    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    Employee reviewer;
}
