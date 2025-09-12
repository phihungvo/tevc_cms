package carevn.luv2code.cms.tevc_cms_api.dto;

import java.util.Date;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TimesheetDTO {
    Integer id;

    Integer employeeId;

    String employeeName;

    Integer projectId;

    String projectName;

    Date date;

    Double hoursWorked;

    String description;

    String status;

    Integer approverId;

    String approverName;

    String comments;

    Date submissionDate;

    Date approvalDate;
}
