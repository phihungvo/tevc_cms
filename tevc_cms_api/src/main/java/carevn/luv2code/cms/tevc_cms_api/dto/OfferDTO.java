package carevn.luv2code.cms.tevc_cms_api.dto;

import java.util.Date;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OfferDTO {
    Integer id;

    Date offerDate;

    Double salary;

    String benefits;

    String offerStatus;

    Integer candidateId;

    String candidateName;

    Integer jobPostingId;
}
