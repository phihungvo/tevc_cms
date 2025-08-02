package carevn.luv2code.cms.tevc_cms_api.service;

import carevn.luv2code.cms.tevc_cms_api.dto.PayrollDetailDTO;

import java.util.List;
import java.util.UUID;

public interface PayrollDetailService {

    PayrollDetailDTO createPayrollDetail(PayrollDetailDTO payrollDetailDTO);

    PayrollDetailDTO updatePayrollDetail(UUID id, PayrollDetailDTO payrollDetailDTO);

    void deletePayrollDetail(UUID id);

    PayrollDetailDTO getPayrollDetail(UUID id);

    List<PayrollDetailDTO> getPayrollDetails(UUID payrollId);

    List<PayrollDetailDTO> getPayrollDetailsByType(String type);
}
