package carevn.luv2code.cms.tevc_cms_api.dto;

import java.util.UUID;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PositionEmployeeDTO {
    UUID id;

    String firstName;

    String lastName;
}
