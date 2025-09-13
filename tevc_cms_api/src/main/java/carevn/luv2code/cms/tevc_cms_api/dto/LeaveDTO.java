package carevn.luv2code.cms.tevc_cms_api.dto;

import java.util.Date;

import carevn.luv2code.cms.tevc_cms_api.enums.LeaveStatus;
import carevn.luv2code.cms.tevc_cms_api.enums.LeaveType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LeaveDTO {
    Integer id;

    Integer employeeId;

    String employeeName;

    Date startDate;

    Date endDate;

    LeaveType leaveType;

    String reason;

    LeaveStatus leaveStatus;

    String approverComments;
}
