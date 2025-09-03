package carevn.luv2code.cms.tevc_cms_api.dto;

import java.util.Date;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PerformanceDTO {
    Integer id;

    Integer employeeId;

    String employeeName;

    Date reviewDate;

    String reviewPeriod;

    Integer rating;

    String comments;

    String goals;

    String improvements;

    Integer reviewerId;

    String reviewerName;
}
