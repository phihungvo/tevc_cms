package carevn.luv2code.cms.tevc_cms_api.dto;

import java.util.Date;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CandidateDTO {
    Integer id;

    String firstName;

    String lastName;

    String email;

    String phone;

    String resumeUrl;

    Date applicationDate;

    String status;

    Integer jobPostingId;

    List<String> jobTitles;
}
