package carevn.luv2code.cms.tevc_cms_api.dto;

import carevn.luv2code.cms.tevc_cms_api.enums.PositionType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeDTO {
    UUID id;

    String employeeCode;

    String firstName;

    String lastName;

    Date dateOfBirth;

    String gender;

    String email;

    String phone;

    String address;

    Date hireDate;

    UUID departmentId;

    UUID positionId;

    boolean isActive;

}
