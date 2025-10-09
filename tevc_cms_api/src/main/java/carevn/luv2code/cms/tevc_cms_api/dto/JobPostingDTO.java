package carevn.luv2code.cms.tevc_cms_api.dto;

import java.util.Date;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobPostingDTO {

    Integer id;

    String title;

    String description;

    String requirements;

    String location;

    String salaryRange;

    Date postingDate;

    Date closingDate;

    String jobPostingStatus;

    Integer department;

    Integer position;

    Integer recruiter; // employee id

    Integer applicantCount;

    List<Integer> candidateIds;
}
