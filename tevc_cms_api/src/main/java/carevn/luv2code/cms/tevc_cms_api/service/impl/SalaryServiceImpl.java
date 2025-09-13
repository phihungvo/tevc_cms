package carevn.luv2code.cms.tevc_cms_api.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import carevn.luv2code.cms.tevc_cms_api.dto.SalaryDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Employee;
import carevn.luv2code.cms.tevc_cms_api.entity.Salary;
import carevn.luv2code.cms.tevc_cms_api.exception.AppException;
import carevn.luv2code.cms.tevc_cms_api.exception.ErrorCode;
import carevn.luv2code.cms.tevc_cms_api.mapper.SalaryMapper;
import carevn.luv2code.cms.tevc_cms_api.repository.EmployeeRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.SalaryRepository;
import carevn.luv2code.cms.tevc_cms_api.service.SalaryService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalaryServiceImpl implements SalaryService {
    private final SalaryRepository salaryRepository;
    private final EmployeeRepository employeeRepository;
    private final SalaryMapper salaryMapper;

    @Override
    @Transactional
    public SalaryDTO createSalary(SalaryDTO salaryDTO) {
        Employee employee = employeeRepository
                .findById(salaryDTO.getEmployeeId())
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

        Salary salary = salaryMapper.toEntity(salaryDTO);
        salary.setEmployee(employee);

        return salaryMapper.toDTO(salaryRepository.save(salary));
    }

    @Override
    public Page<SalaryDTO> getAllSalaries(int page, int size) {
        Page<Salary> salaryPage = salaryRepository.findAll(PageRequest.of(page, size));
        return salaryPage.map(salaryMapper::toDTO);
    }

    @Override
    public SalaryDTO updateSalary(Integer id, SalaryDTO salaryDTO) {
        Salary existingSalary =
                salaryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.SALARY_NOT_FOUND));

        Employee employee = employeeRepository
                .findById(salaryDTO.getEmployeeId())
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

        salaryMapper.updateSalaryFromDto(salaryDTO, existingSalary);
        existingSalary.setEmployee(employee);

        return salaryMapper.toDTO(salaryRepository.save(existingSalary));
    }

    @Override
    public SalaryDTO getSalary(Integer id) {
        return null;
    }

    @Override
    public List<SalaryDTO> getEmployeeSalaries(Integer employeeId) {
        return List.of();
    }

    @Override
    public SalaryDTO calculateSalary(Integer employeeId, String period) {
        return null;
    }

    @Override
    public void deleteSalary(Integer id) {}
}
