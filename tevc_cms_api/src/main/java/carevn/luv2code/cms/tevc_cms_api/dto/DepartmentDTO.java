package carevn.luv2code.cms.tevc_cms_api.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DepartmentDTO {
    Integer id;

    String name;

    String description;

    Integer managerId;

    int employeeCount;

    String managerName;
}
