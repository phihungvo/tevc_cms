package carevn.luv2code.cms.tevc_cms_api.dto;

import java.util.Date;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SalaryDTO {
    Integer id;

    Integer employeeId;

    String employeeName;

    Date paymentDate;

    Double basicSalary;

    Double bonus;

    Double deductions;

    Double netSalary;

    String paymentStatus;

    String period;
}
