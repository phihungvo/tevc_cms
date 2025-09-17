package carevn.luv2code.cms.tevc_cms_api.dto;

import java.util.Date;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeDTO {
    Integer id;

    String employeeCode;

    String firstName;

    String lastName;

    Date dateOfBirth;

    String gender;

    String email;

    String phone;

    String address;

    String profilePicture;

    Date hireDate;

    Integer departmentId;

    Integer positionId;

    boolean isActive;
}
