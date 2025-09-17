package carevn.luv2code.cms.tevc_cms_api.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PositionDTO {
    Integer id;

    String title;

    String description;

    Double baseSalary;

    String positionType;

    //    int employeeCount;
}
