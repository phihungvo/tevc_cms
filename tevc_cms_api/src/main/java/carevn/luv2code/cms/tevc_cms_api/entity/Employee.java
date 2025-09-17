package carevn.luv2code.cms.tevc_cms_api.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "employees")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false, unique = true)
    String employeeCode;

    String firstName;

    String lastName;

    Date dateOfBirth;

    String gender;

    String email;

    String phone;

    String address;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_picture_id", referencedColumnName = "id")
    private File profilePicture;

    @Column(name = "hire_date")
    Date hireDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @JsonBackReference
    Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    @JsonBackReference
    Position position;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    Set<Attendance> attendances = new HashSet<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    Set<Leave> leaves = new HashSet<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    Set<Salary> salaries = new HashSet<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    Set<Performance> performances = new HashSet<>();

    @OneToMany(
            mappedBy = "manager",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY)
    @JsonManagedReference
    Set<Department> managedDepartments = new HashSet<>();

    boolean isActive;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    Date updatedAt;
}
