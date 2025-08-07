package carevn.luv2code.cms.tevc_cms_api.service.impl;

import carevn.luv2code.cms.tevc_cms_api.dto.EmployeeDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Employee;
import carevn.luv2code.cms.tevc_cms_api.enums.PositionType;
import carevn.luv2code.cms.tevc_cms_api.exception.AppException;
import carevn.luv2code.cms.tevc_cms_api.exception.ErrorCode;
import carevn.luv2code.cms.tevc_cms_api.mapper.EmployeeMapper;
import carevn.luv2code.cms.tevc_cms_api.repository.EmployeeRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.PositionRepository;
import carevn.luv2code.cms.tevc_cms_api.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        if (employeeRepository.existsByEmployeeCode(employeeDTO.getEmployeeCode())) {
            throw new AppException(ErrorCode.EMPLOYEE_CODE_EXISTS);
        }

        String generatedCode = generateNextEmployeeCode();
        employeeDTO.setEmployeeCode(generatedCode);

        Employee employee = employeeMapper.toEntity(employeeDTO);
        employee.setCreatedAt(new Date());
        employee.setActive(true);

        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDTO(savedEmployee);
    }

    @Override
    @Transactional
    public EmployeeDTO updateEmployee(UUID id, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

        employeeMapper.updateEmployeeFromDto(employeeDTO, employee);
        employee.setUpdatedAt(new Date());

        Employee updatedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDTO(updatedEmployee);
    }

    @Override
    public EmployeeDTO getEmployee(UUID id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
        return employeeMapper.toDTO(employee);
    }

    @Override
    public Page<EmployeeDTO> getAllEmployees(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return employeeRepository.findAll(pageRequest)
                .map(employeeMapper::toDTO);
    }


//    @Override
//    public Page<EmployeeDTO> findByDepartment(UUID departmentId, int page, int size) {
//        PageRequest pageRequest = PageRequest.of(page, size);
//        return employeeRepository.findAll(pageRequest)
//                .map(employeeMapper::toDTO)
//                .filter(dto -> departmentId.equals(dto.getDepartmentId()));
//    }

    @Override
    @Transactional
    public void deleteEmployee(UUID id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

        // Check if employee is a department manager
        if (!employee.getManagedDepartments().isEmpty()) {
            throw new AppException(ErrorCode.DEPARTMENT_HAS_EMPLOYEES);
        }

        employeeRepository.delete(employee);
    }

    @Override
    @Transactional
    public boolean toggleEmployeeStatus(UUID id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
        employee.setActive(!employee.isActive());
        employee.setUpdatedAt(new Date());
        employeeRepository.save(employee);
        return employee.isActive();
    }

    @Override
    public List<Employee> getEmployeesByPositionType(PositionType positionType) {
        return employeeRepository.findByPositionType(positionType);
    }

    private String generateNextEmployeeCode() {
        String lastCode = employeeRepository.findMaxEmployeeCode();
        int nextNumber = 1;

        if (lastCode != null && lastCode.startsWith("EMP")) {
            int currentNumber = Integer.parseInt(lastCode.substring(3));
            nextNumber = currentNumber + 1;
        }

        return String.format("EMP%04d", nextNumber);
    }

}
