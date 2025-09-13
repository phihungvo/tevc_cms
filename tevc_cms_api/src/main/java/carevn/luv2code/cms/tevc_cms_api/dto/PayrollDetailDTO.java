package carevn.luv2code.cms.tevc_cms_api.dto;

import java.util.Date;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PayrollDetailDTO {
    Integer id;

    Integer payrollId;

    String type;

    String description;

    Double amount;

    Date date;

    String category;
}
