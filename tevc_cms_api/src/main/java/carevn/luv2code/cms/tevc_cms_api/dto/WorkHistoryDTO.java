package carevn.luv2code.cms.tevc_cms_api.dto;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkHistoryDTO {
    Integer id;

    Integer employeeId;

    String companyName;

    String position;

    LocalDate startDate;

    LocalDate endDate;

    String description;

    String companyAddress;

    String reasonForLeaving;

    Double salary;

    String contractType;

    String supervisorName;
}
