package carevn.luv2code.cms.tevc_cms_api.dto;

import java.util.Date;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobPostingDTO {

    UUID id;

    String title;

    String description;

    String requirements;

    String location;

    String salaryRange;

    Date postingDate;

    Date closingDate;

    String jobPostingStatus;

    UUID department;

    UUID position;

    UUID recruiter; // employee id
}
