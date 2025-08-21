package carevn.luv2code.cms.tevc_cms_api.dto;

import java.util.Date;
import java.util.UUID;

import carevn.luv2code.cms.tevc_cms_api.enums.AttendanceStatus;
import lombok.Data;

@Data
public class AttendanceDTO {
    private UUID id;
    private UUID employeeId;
    private Date checkIn;
    private Date checkOut;
    private AttendanceStatus status;
    private String notes;
}
