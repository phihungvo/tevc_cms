package carevn.luv2code.cms.tevc_cms_api.service;

import java.util.List;

import org.springframework.data.domain.Page;

import carevn.luv2code.cms.tevc_cms_api.dto.PayrollDTO;

public interface PayrollService {

    PayrollDTO createPayroll(PayrollDTO payrollDTO);

    PayrollDTO updatePayroll(Integer id, PayrollDTO payrollDTO);

    void deletePayroll(Integer id);

    PayrollDTO getPayroll(Integer id);

    Page<PayrollDTO> getAllPayrolls(int page, int size);

    PayrollDTO processPayroll(Integer id);

    PayrollDTO finalizePayroll(Integer id);

    List<PayrollDTO> getEmployeePayrolls(Integer employeeId);

    PayrollDTO calculatePayroll(Integer employeeId, String period);
}
