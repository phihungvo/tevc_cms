package carevn.luv2code.cms.tevc_cms_api.dto;

import java.util.Date;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttendanceDTO {
    Integer id;

    Integer employeeId;

    String employeeName;

    String employeeCode;

    Date attendanceDate;

    Date checkIn;

    Date checkOut;

    Double workHours;

    String status;

    String notes;
}
