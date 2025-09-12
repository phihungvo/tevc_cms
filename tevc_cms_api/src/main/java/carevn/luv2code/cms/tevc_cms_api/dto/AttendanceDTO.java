package carevn.luv2code.cms.tevc_cms_api.dto;

import java.util.Date;

import carevn.luv2code.cms.tevc_cms_api.enums.AttendanceStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttendanceDTO {
    Integer id;

    Integer employeeId;

    String employeeName;

    Date checkIn;

    Date checkOut;

    AttendanceStatus status;

    String notes;
}
