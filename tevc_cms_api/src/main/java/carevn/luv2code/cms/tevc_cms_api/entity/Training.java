package carevn.luv2code.cms.tevc_cms_api.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "trainings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Training {
    @Id
    @GeneratedValue
    UUID id;

    @Column(nullable = false)
    String name;

    String description;

    Date startDate;

    Date endDate;

    String trainer;

    String location;

    Double cost;

    String status;

    @ManyToMany
    @JoinTable(
            name = "employee_trainings",
            joinColumns = @JoinColumn(name = "training_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id"))
    @ToString.Exclude
    Set<Employee> participants = new HashSet<>();
}
