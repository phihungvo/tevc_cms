package carevn.luv2code.cms.tevc_cms_api.dto;

import java.util.Set;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TeamDTO {
    Integer id;

    String name;

    String description;

    Integer departmentId;

    Set<Integer> employeeIds;
}
