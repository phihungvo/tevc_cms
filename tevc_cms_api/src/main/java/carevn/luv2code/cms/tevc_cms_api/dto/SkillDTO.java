package carevn.luv2code.cms.tevc_cms_api.dto;

import java.time.LocalDateTime;
import java.util.List;

import carevn.luv2code.cms.tevc_cms_api.enums.SkillLevel;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SkillDTO {
    Integer id;

    String name;

    String description;

    SkillLevel level;

    List<Integer> employeesIds;

    LocalDateTime lastUpdated;
}
