package carevn.luv2code.cms.tevc_cms_api.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import carevn.luv2code.cms.tevc_cms_api.dto.PayrollDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Employee;
import carevn.luv2code.cms.tevc_cms_api.entity.Payroll;
import carevn.luv2code.cms.tevc_cms_api.enums.PayrollStatus;
import carevn.luv2code.cms.tevc_cms_api.exception.AppException;
import carevn.luv2code.cms.tevc_cms_api.exception.ErrorCode;
import carevn.luv2code.cms.tevc_cms_api.mapper.PayrollMapper;
import carevn.luv2code.cms.tevc_cms_api.repository.EmployeeRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.PayrollRepository;
import carevn.luv2code.cms.tevc_cms_api.service.PayrollService;
import carevn.luv2code.cms.tevc_cms_api.service.TimesheetService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayrollServiceImpl implements PayrollService {
    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;
    private final PayrollMapper payrollMapper;
    private final TimesheetService timesheetService;

    private static final Pattern PERIOD_PATTERN = Pattern.compile("\\d{4}-\\d{2}");

    @Override
    @Transactional
    public PayrollDTO createPayroll(PayrollDTO payrollDTO) {
        if (payrollDTO.getEmployeeId() == null) {
            throw new AppException(ErrorCode.EMPLOYEE_NOT_FOUND);
        }
        if (payrollDTO.getPeriod() == null
                || !PERIOD_PATTERN.matcher(payrollDTO.getPeriod()).matches()) {
            throw new AppException(ErrorCode.INVALID_PAYROLL_PERIOD);
        }

        Employee employee = employeeRepository
                .findById(payrollDTO.getEmployeeId())
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

        Payroll payroll = payrollMapper.toEntity(payrollDTO);
        payroll.setEmployee(employee);
        payroll.setStatus(PayrollStatus.PENDING);
        payroll.setProcessedDate(new Date());

        Payroll savedPayroll = payrollRepository.save(payroll);

        PayrollDTO result = payrollMapper.toDTO(savedPayroll);

        return result;
    }

    @Override
    @Transactional
    public PayrollDTO updatePayroll(UUID id, PayrollDTO payrollDTO) {
        Payroll payroll =
                payrollRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PAYROLL_NOT_FOUND));
        payrollMapper.updateFromDto(payrollDTO, payroll);
        return payrollMapper.toDTO(payrollRepository.save(payroll));
    }

    @Override
    public void deletePayroll(UUID id) {}

    @Override
    public PayrollDTO getPayroll(UUID id) {
        return payrollMapper.toDTO(
                payrollRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PAYROLL_NOT_FOUND)));
    }

    @Override
    public Page<PayrollDTO> getAllPayrolls(int page, int size) {
        return payrollRepository.findAll(PageRequest.of(page, size)).map(payrollMapper::toDTO);
    }

    @Override
    @Transactional
    public PayrollDTO processPayroll(UUID id) {
        Payroll payroll =
                payrollRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PAYROLL_NOT_FOUND));

        if (!PayrollStatus.PENDING.equals(payroll.getStatus())) {
            throw new AppException(ErrorCode.PAYROLL_ALREADY_PROCESSED);
        }

        // Calculate salary components
        calculateSalaryComponents(payroll);

        payroll.setStatus(PayrollStatus.PROCESSED);
        payroll.setProcessedDate(new Date());

        return payrollMapper.toDTO(payrollRepository.save(payroll));
    }

    @Override
    @Transactional
    public PayrollDTO finalizePayroll(UUID id) {
        Payroll payroll =
                payrollRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PAYROLL_NOT_FOUND));

        if (!PayrollStatus.PROCESSED.equals(payroll.getStatus())) {
            throw new AppException(ErrorCode.PAYROLL_ALREADY_PROCESSED);
        }

        payroll.setStatus(PayrollStatus.PAID);
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
        if (period == null || !PERIOD_PATTERN.matcher(period).matches()) {
            throw new AppException(ErrorCode.INVALID_PAYROLL_PERIOD);
        }

        Employee employee = employeeRepository
                .findById(employeeId)
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

        if (payrollRepository.findByEmployeeIdAndPeriod(employeeId, period).isPresent()) {
            throw new AppException(ErrorCode.PAYROLL_ALREADY_EXISTS);
        }

        if (employee.getPosition() == null) {
            throw new AppException(ErrorCode.POSITION_NOT_FOUND);
        }

        PayrollDTO payrollDTO = new PayrollDTO();
        payrollDTO.setEmployeeId(employeeId);
        payrollDTO.setPeriod(period);

        // Lấy lương cơ bản từ position
        payrollDTO.setBasicSalary(employeeRepository
                .findById(employeeId)
                .map(emp -> emp.getPosition() != null ? emp.getPosition().getBaseSalary() : null)
                .orElseThrow(() -> new AppException(ErrorCode.POSITION_NOT_FOUND)));

        // Tính giờ làm thêm
        double overtime = calculateOvertime(employeeId, period);
        payrollDTO.setOvertime(overtime);

        // Tính các thành phần lương khác (giả định từ cấu hình hoặc tham số)
        payrollDTO.setBonus(calculateBonus(employee, period));
        payrollDTO.setAllowances(calculateAllowances(employee, period));
        payrollDTO.setDeductions(calculateDeductions(employee, period));
        payrollDTO.setTax(calculateTax(employee, period));
        payrollDTO.setInsurance(calculateInsurance(employee, period));

        // Tính lương ròng
        calculateNetSalary(payrollDTO);

        // Tạo chi tiết lương
        // payrollDTO.setDetails(createPayrollDetails(payrollDTO));

        // Tạo bảng lương
        PayrollDTO result = createPayroll(payrollDTO);

        return result;
    }

    private double calculateBonus(Employee employee, String period) {
        // Implement logic to calculate bonus based on employee's performance or company policy
        return 0.0;
    }

    private double calculateAllowances(Employee employee, String period) {
        // Implement logic to calculate allowances based on employee's position or company policy
        return 0.0;
    }

    private double calculateDeductions(Employee employee, String period) {
        // Implement logic to calculate deductions based on employee's loans or other deductions
        return 0.0;
    }

    private double calculateTax(Employee employee, String period) {
        double income =
                (employee.getPosition() != null && employee.getPosition().getBaseSalary() != null
                        ? employee.getPosition().getBaseSalary()
                        : 0.0);
        return income * 0.1;
    }

    private double calculateInsurance(Employee employee, String period) {
        // Giả định: Bảo hiểm 8% của lương cơ bản
        return (employee.getPosition() != null && employee.getPosition().getBaseSalary() != null
                        ? employee.getPosition().getBaseSalary()
                        : 0.0)
                * 0.08;
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
        try {
            return timesheetService.calculateOvertimeForEmployee(employeeId, period);
        } catch (Exception e) {
            return 0.0;
        }
    }

    private void calculateNetSalary(PayrollDTO payrollDTO) {
        double netSalary = payrollDTO.getBasicSalary() != null ? payrollDTO.getBasicSalary() : 0.0;

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

    //    private List<PayrollDetailDTO> createPayrollDetails(PayrollDTO payrollDTO) {
    //        return List.of(
    //                        new PayrollDetailDTO("Basic Salary", payrollDTO.getBasicSalary()),
    //                        new PayrollDetailDTO("Overtime", payrollDTO.getOvertime()),
    //                        new PayrollDetailDTO("Bonus", payrollDTO.getBonus()),
    //                        new PayrollDetailDTO("Allowances", payrollDTO.getAllowances()),
    //                        new PayrollDetailDTO("Deductions", payrollDTO.getDeductions()),
    //                        new PayrollDetailDTO("Tax", payrollDTO.getTax()),
    //                        new PayrollDetailDTO("Insurance", payrollDTO.getInsurance())
    //                ).stream()
    //                .filter(detail -> detail.getAmount() != null && detail.getAmount() != 0.0)
    //                .collect(Collectors.toList());
    //    }
}
