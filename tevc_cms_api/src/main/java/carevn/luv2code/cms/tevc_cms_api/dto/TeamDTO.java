package carevn.luv2code.cms.tevc_cms_api.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TeamDTO {
    Integer id;

    @NotBlank(message = "Team name is required")
    String name;

    String description;

    @NotNull(message = "Department ID is required")
    Integer departmentId;

    Set<Integer> employeeIds;
}
