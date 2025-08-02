package carevn.luv2code.cms.tevc_cms_api.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DepartmentDTO {
    UUID id;

    String name;

    String description;

    UUID managerId;

    String managerName;

    int employeeCount;

}
