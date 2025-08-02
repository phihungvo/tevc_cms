package carevn.luv2code.cms.tevc_cms_api.service.impl;

import carevn.luv2code.cms.tevc_cms_api.dto.PayrollDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Employee;
import carevn.luv2code.cms.tevc_cms_api.entity.Payroll;
import carevn.luv2code.cms.tevc_cms_api.exception.AppException;
import carevn.luv2code.cms.tevc_cms_api.exception.ErrorCode;
import carevn.luv2code.cms.tevc_cms_api.mapper.PayrollMapper;
import carevn.luv2code.cms.tevc_cms_api.repository.EmployeeRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.PayrollRepository;
import carevn.luv2code.cms.tevc_cms_api.service.PayrollService;
import carevn.luv2code.cms.tevc_cms_api.service.TimesheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayrollServiceImpl implements PayrollService {
    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;
    private final PayrollMapper payrollMapper;
    private final TimesheetService timesheetService;

    @Override
    @Transactional
    public PayrollDTO createPayroll(PayrollDTO payrollDTO) {
        Payroll payroll = payrollMapper.toEntity(payrollDTO);
        payroll.setStatus("PENDING");
        payroll.setProcessedDate(new Date());
        return payrollMapper.toDTO(payrollRepository.save(payroll));
    }

    @Override
    @Transactional
    public PayrollDTO updatePayroll(UUID id, PayrollDTO payrollDTO) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PAYROLL_NOT_FOUND));
        payrollMapper.updateFromDto(payrollDTO, payroll);
        return payrollMapper.toDTO(payrollRepository.save(payroll));
    }

    @Override
    public PayrollDTO getPayroll(UUID id) {
        return payrollMapper.toDTO(payrollRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PAYROLL_NOT_FOUND)));
    }

    @Override
    public Page<PayrollDTO> getAllPayrolls(int page, int size) {
        return payrollRepository.findAll(PageRequest.of(page, size))
                .map(payrollMapper::toDTO);
    }

    @Override
    @Transactional
    public PayrollDTO processPayroll(UUID id) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PAYROLL_NOT_FOUND));
        
        if (!"PENDING".equals(payroll.getStatus())) {
            throw new AppException(ErrorCode.PAYROLL_ALREADY_PROCESSED);
        }

        // Calculate salary components
        calculateSalaryComponents(payroll);
        
        payroll.setStatus("PROCESSED");
        payroll.setProcessedDate(new Date());
        
        return payrollMapper.toDTO(payrollRepository.save(payroll));
    }

    @Override
    @Transactional
    public PayrollDTO finalizePayroll(UUID id) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PAYROLL_NOT_FOUND));
        
        if (!"PROCESSED".equals(payroll.getStatus())) {
            throw new AppException(ErrorCode.PAYROLL_ALREADY_PROCESSED);
        }

        payroll.setStatus("PAID");
        payroll.setPaidDate(new Date());
        
        return payrollMapper.toDTO(payrollRepository.save(payroll));
    }

    @Override
    public List<PayrollDTO> getEmployeePayrolls(UUID employeeId) {
        return payrollRepository.findByEmployeeId(employeeId).stream()
                .map(payrollMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PayrollDTO calculatePayroll(UUID employeeId, String period) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

        if (payrollRepository.findByEmployeeIdAndPeriod(employeeId, period).isPresent()) {
            throw new AppException(ErrorCode.PAYROLL_ALREADY_EXISTS);
        }

        PayrollDTO payrollDTO = new PayrollDTO();
        payrollDTO.setEmployeeId(employeeId);
        payrollDTO.setPeriod(period);
        
        // Get base salary from employee's position
        payrollDTO.setBasicSalary(employee.getPosition().getBaseSalary());
        
        // Calculate overtime from timesheets
        double overtime = calculateOvertime(employeeId, period);
        payrollDTO.setOvertime(overtime);
        
        // Calculate net salary
        calculateNetSalary(payrollDTO);
        
        return createPayroll(payrollDTO);
    }

    private void calculateSalaryComponents(Payroll payroll) {
        // Implementation for salary calculation
        double netSalary = payroll.getBasicSalary();
        
        if (payroll.getOvertime() != null) {
            netSalary += payroll.getOvertime();
        }
        if (payroll.getBonus() != null) {
            netSalary += payroll.getBonus();
        }
        if (payroll.getAllowances() != null) {
            netSalary += payroll.getAllowances();
        }
        if (payroll.getDeductions() != null) {
            netSalary -= payroll.getDeductions();
        }
        if (payroll.getTax() != null) {
            netSalary -= payroll.getTax();
        }
        if (payroll.getInsurance() != null) {
            netSalary -= payroll.getInsurance();
        }
        
        payroll.setNetSalary(netSalary);
    }

    private double calculateOvertime(UUID employeeId, String period) {
        // Implementation for overtime calculation from timesheets
        return 0.0;
    }

    private void calculateNetSalary(PayrollDTO payrollDTO) {
        double netSalary = payrollDTO.getBasicSalary();
        
        // Add other components
        if (payrollDTO.getOvertime() != null) netSalary += payrollDTO.getOvertime();
        if (payrollDTO.getBonus() != null) netSalary += payrollDTO.getBonus();
        if (payrollDTO.getAllowances() != null) netSalary += payrollDTO.getAllowances();
        
        // Subtract deductions
        if (payrollDTO.getDeductions() != null) netSalary -= payrollDTO.getDeductions();
        if (payrollDTO.getTax() != null) netSalary -= payrollDTO.getTax();
        if (payrollDTO.getInsurance() != null) netSalary -= payrollDTO.getInsurance();
        
        payrollDTO.setNetSalary(netSalary);
    }
}
