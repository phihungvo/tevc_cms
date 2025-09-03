package carevn.luv2code.cms.tevc_cms_api.dto;

import java.util.Date;

import carevn.luv2code.cms.tevc_cms_api.enums.InterviewType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InterviewDTO {

    Integer id;

    Date interviewDate;

    InterviewType interviewType;

    Integer interviewerId;

    String feedback;

    Integer rating;

    String interviewStatus;

    //    Candidate candidate;
    Integer candidateId;

    String candidateName;

    Integer jobPostingId;
}
