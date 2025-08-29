package carevn.luv2code.cms.tevc_cms_api.entity;

import java.util.Date;
import java.util.UUID;

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
    @GeneratedValue
    UUID id;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_posting_id", nullable = false)
    JobPosting jobPosting;
}
