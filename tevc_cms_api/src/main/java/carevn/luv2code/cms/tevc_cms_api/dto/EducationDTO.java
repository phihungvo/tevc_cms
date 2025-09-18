package carevn.luv2code.cms.tevc_cms_api.dto;

import java.time.LocalDate;

import carevn.luv2code.cms.tevc_cms_api.enums.DegreeLevel;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EducationDTO {
    Integer id;

    Integer employeeId;

    String institutionName;

    String major;

    DegreeLevel degree;

    LocalDate startDate;

    LocalDate graduationDate;

    Float gpa;
}
