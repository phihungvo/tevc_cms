package carevn.luv2code.cms.tevc_cms_api.service;

import java.util.List;

import org.springframework.data.domain.Page;

import carevn.luv2code.cms.tevc_cms_api.dto.SalaryDTO;

public interface SalaryService {
    SalaryDTO createSalary(SalaryDTO salaryDTO);

    SalaryDTO updateSalary(Integer id, SalaryDTO salaryDTO);

    SalaryDTO getSalary(Integer id);

    Page<SalaryDTO> getAllSalaries(int page, int size);

    List<SalaryDTO> getEmployeeSalaries(Integer employeeId);

    SalaryDTO calculateSalary(Integer employeeId, String period);

    void deleteSalary(Integer id);
}
