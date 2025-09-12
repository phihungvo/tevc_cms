package carevn.luv2code.cms.tevc_cms_api.service;

import java.util.List;

import carevn.luv2code.cms.tevc_cms_api.dto.PayrollDetailDTO;

public interface PayrollDetailService {

    PayrollDetailDTO createPayrollDetail(PayrollDetailDTO payrollDetailDTO);

    PayrollDetailDTO updatePayrollDetail(Integer id, PayrollDetailDTO payrollDetailDTO);

    void deletePayrollDetail(Integer id);

    PayrollDetailDTO getPayrollDetail(Integer id);

    List<PayrollDetailDTO> getPayrollDetails(Integer payrollId);

    List<PayrollDetailDTO> getPayrollDetailsByType(String type);
}
