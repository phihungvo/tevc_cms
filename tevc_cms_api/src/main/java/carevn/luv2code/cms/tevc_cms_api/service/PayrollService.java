package carevn.luv2code.cms.tevc_cms_api.service;

import carevn.luv2code.cms.tevc_cms_api.dto.PayrollDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface PayrollService {

    PayrollDTO createPayroll(PayrollDTO payrollDTO);

    PayrollDTO updatePayroll(UUID id, PayrollDTO payrollDTO);

    void deletePayroll(UUID id);

    PayrollDTO getPayroll(UUID id);

    Page<PayrollDTO> getAllPayrolls(int page, int size);

    PayrollDTO processPayroll(UUID id);

    PayrollDTO finalizePayroll(UUID id);

    List<PayrollDTO> getEmployeePayrolls(UUID employeeId);

    PayrollDTO calculatePayroll(UUID employeeId, String period);
}
