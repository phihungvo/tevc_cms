package carevn.luv2code.cms.tevc_cms_api.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PayrollDTO {
    UUID id;

    UUID employeeId;

    String employeeName;

    String period;

    Double basicSalary;

    Double overtime;

    Double bonus;

    Double allowances;

    Double deductions;

    Double tax;

    Double insurance;

    Double netSalary;

    String status;

    Date processedDate;

    Date paidDate;

    List<PayrollDetailDTO> details;
}
