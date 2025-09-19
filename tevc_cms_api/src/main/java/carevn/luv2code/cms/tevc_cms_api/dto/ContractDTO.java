package carevn.luv2code.cms.tevc_cms_api.dto;

import java.time.LocalDate;
import java.util.Set;

import carevn.luv2code.cms.tevc_cms_api.enums.ContractStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContractDTO {

    Integer id;

    Integer employeeId;

    String contractType;

    /**
     * Thời gian hiệu lực
     */
    LocalDate startDate;

    LocalDate endDate;

    Double basicSalary;

    Integer positionId;
    //    String positionTitle;

    ContractStatus status;

    LocalDate signedDate;

    Integer probationPeriod;

    /**
     * Lý do chấm dứt hợp đồng
     */
    String terminationReason;

    LocalDate terminationDate;

    Set<Integer> fileIds;

    //    Set<File> files;

    Integer createdById;
}
