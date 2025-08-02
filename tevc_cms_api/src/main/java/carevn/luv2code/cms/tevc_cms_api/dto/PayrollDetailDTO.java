package carevn.luv2code.cms.tevc_cms_api.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PayrollDetailDTO {
    UUID id;

    UUID payrollId;

    String type;

    String description;

    Double amount;

    Date date;

    String category;

}
