package carevn.luv2code.cms.tevc_cms_api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import carevn.luv2code.cms.tevc_cms_api.dto.SalaryDTO;

public interface SalaryService {
    SalaryDTO createSalary(SalaryDTO salaryDTO);

    SalaryDTO updateSalary(UUID id, SalaryDTO salaryDTO);

    SalaryDTO getSalary(UUID id);

    Page<SalaryDTO> getAllSalaries(int page, int size);

    List<SalaryDTO> getEmployeeSalaries(UUID employeeId);

    SalaryDTO calculateSalary(UUID employeeId, String period);

    void deleteSalary(UUID id);
}
