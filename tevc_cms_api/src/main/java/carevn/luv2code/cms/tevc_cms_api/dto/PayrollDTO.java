package carevn.luv2code.cms.tevc_cms_api.dto;

import java.util.Date;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PayrollDTO {
    Integer id;

    Integer employeeId;

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
