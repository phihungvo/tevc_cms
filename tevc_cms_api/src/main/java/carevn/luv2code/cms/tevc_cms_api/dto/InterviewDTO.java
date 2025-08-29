package carevn.luv2code.cms.tevc_cms_api.dto;

import java.util.Date;
import java.util.UUID;

import carevn.luv2code.cms.tevc_cms_api.enums.InterviewType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InterviewDTO {

    UUID id;

    Date interviewDate;

    InterviewType interviewType;

    UUID interviewerId;

    String feedback;

    Integer rating;

    String interviewStatus;

    //    Candidate candidate;
    UUID candidateId;

    String candidateName;

    UUID jobPostingId;
}
