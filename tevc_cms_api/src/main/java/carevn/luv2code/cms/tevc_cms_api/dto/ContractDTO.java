package carevn.luv2code.cms.tevc_cms_api.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContractDTO {
    UUID id;

    UUID employeeId;

    String employeeName;

    String contractType;

    Date startDate;

    Date endDate;

    Double basicSalary;

    String position;

    String terms;

    String status;

    Date signedDate;

    Integer probationPeriod;

    String terminationReason;

    Date terminationDate;

}
