package carevn.luv2code.cms.tevc_cms_api.entity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import carevn.luv2code.cms.tevc_cms_api.enums.JobPostingStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "job_postings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobPosting {

    @Id
    @GeneratedValue
    UUID id;

    @Column(nullable = false)
    String title;

    String description;

    String requirements;

    String location;

    String salaryRange;

    @Column(name = "posting_date")
    Date postingDate;

    @Column(name = "closing_date")
    Date closingDate;

    @Enumerated(EnumType.STRING)
    JobPostingStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    Position position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id")
    Employee recruiter;

    @ManyToMany(mappedBy = "jobPostings")
    List<Candidate> candidates;
}
