package carevn.luv2code.cms.tevc_cms_api.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PerformanceDTO {
    UUID id;

    UUID employeeId;

    String employeeName;

    Date reviewDate;

    String reviewPeriod;

    Integer rating;

    String comments;

    String goals;

    String improvements;

    UUID reviewerId;

    String reviewerName;
}
