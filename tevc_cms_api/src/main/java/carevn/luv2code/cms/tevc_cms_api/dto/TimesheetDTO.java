package carevn.luv2code.cms.tevc_cms_api.dto;

import java.util.Date;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TimesheetDTO {
    UUID id;

    UUID employeeId;

    String employeeName;

    UUID projectId;

    String projectName;

    Date date;

    Double hoursWorked;

    String description;

    String status;

    UUID approverId;

    String approverName;

    String comments;

    Date submissionDate;

    Date approvalDate;
}
