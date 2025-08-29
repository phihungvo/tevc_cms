package carevn.luv2code.cms.tevc_cms_api.dto;

import java.util.Date;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OfferDTO {
    UUID id;

    Date offerDate;

    Double salary;

    String benefits;

    String offerStatus;

    UUID candidateId;

    String candidateName;

    UUID jobPostingId;
}
