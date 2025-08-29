package carevn.luv2code.cms.tevc_cms_api.entity;

import java.util.Date;
import java.util.UUID;

import carevn.luv2code.cms.tevc_cms_api.enums.InterviewStatus;
import carevn.luv2code.cms.tevc_cms_api.enums.InterviewType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "interviews")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Interview {

    @Id
    @GeneratedValue
    UUID id;

    @Column(name = "interview_date")
    Date interviewDate;

    @Enumerated(EnumType.STRING)
    InterviewType interviewType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interviewer_id")
    Employee interviewer;

    String feedback;

    Integer rating;

    @Enumerated(EnumType.STRING)
    InterviewStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    Candidate candidate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_posting_id", nullable = false)
    JobPosting jobPosting;
}
