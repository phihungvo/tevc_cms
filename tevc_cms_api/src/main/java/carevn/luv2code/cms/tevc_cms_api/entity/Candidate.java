package carevn.luv2code.cms.tevc_cms_api.entity;

import java.util.Date;
import java.util.List;

import carevn.luv2code.cms.tevc_cms_api.enums.CandidateStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "candidates")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "first_name", nullable = false)
    String firstName;

    @Column(name = "last_name", nullable = false)
    String lastName;

    String email;

    String phone;

    @Column(name = "resume_url")
    String resumeUrl;

    @Column(name = "application_date")
    Date applicationDate;

    @Enumerated(EnumType.STRING)
    CandidateStatus status;

    @ManyToMany
    @JoinTable(
            name = "candidate_job_posting",
            joinColumns = @JoinColumn(name = "candidate_id"),
            inverseJoinColumns = @JoinColumn(name = "job_posting_id"))
    List<JobPosting> jobPostings;
}
