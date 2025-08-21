package carevn.luv2code.cms.tevc_cms_api.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import carevn.luv2code.cms.tevc_cms_api.dto.EmployeeDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Department;
import carevn.luv2code.cms.tevc_cms_api.entity.Employee;
import carevn.luv2code.cms.tevc_cms_api.entity.Position;
import carevn.luv2code.cms.tevc_cms_api.enums.PositionType;
import carevn.luv2code.cms.tevc_cms_api.exception.AppException;
import carevn.luv2code.cms.tevc_cms_api.exception.ErrorCode;
import carevn.luv2code.cms.tevc_cms_api.mapper.EmployeeMapper;
import carevn.luv2code.cms.tevc_cms_api.repository.DepartmentRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.EmployeeRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.PositionRepository;
import carevn.luv2code.cms.tevc_cms_api.service.EmployeeService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final EmployeeMapper employeeMapper;
    private final DepartmentRepository departmentRepository;

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
        employee.setDepartment(departmentRepository
                .findById(employeeDTO.getDepartmentId())
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND)));
        employee.setPosition(positionRepository
                .findById(employeeDTO.getPositionId())
                .orElseThrow(() -> new AppException(ErrorCode.POSITION_NOT_FOUND)));
        employee.setCreatedAt(new Date());
        employee.setActive(true);

        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDTO(savedEmployee);
    }

    @Override
    @Transactional
    public EmployeeDTO updateEmployee(UUID id, EmployeeDTO employeeDTO) {
        Employee employee =
                employeeRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

        Department department = departmentRepository
                .findById(employeeDTO.getDepartmentId())
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));

        Position position = positionRepository
                .findById(employeeDTO.getPositionId())
                .orElseThrow(() -> new AppException(ErrorCode.POSITION_NOT_FOUND));

        employeeMapper.updateEmployeeFromDto(employeeDTO, employee);
        employee.setDepartment(department);
        employee.setPosition(position);
        employee.setUpdatedAt(new Date());

        Employee updatedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDTO(updatedEmployee);
    }

    @Override
    public EmployeeDTO getEmployee(UUID id) {
        Employee employee =
                employeeRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
        return employeeMapper.toDTO(employee);
    }

    @Override
    public Page<EmployeeDTO> getAllEmployees(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return employeeRepository.findAllWithDepartmentAndPosition(pageRequest).map(employeeMapper::toDTO);
    }

    @Override
    public List<EmployeeDTO> getAllEmployeesNoPaging() {
        return employeeRepository.findAll().stream().map(employeeMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public Page<EmployeeDTO> findByDepartment(UUID departmentId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return employeeRepository.findByDepartmentId(departmentId, pageRequest).map(employeeMapper::toDTO);
    }

    @Override
    @Transactional
    public void deleteEmployee(UUID id) {
        Employee employee =
                employeeRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

        if (!employee.getManagedDepartments().isEmpty()) {
            throw new AppException(ErrorCode.DEPARTMENT_HAS_EMPLOYEES);
        }

        if (employee.getDepartment() != null) {
            employee.setDepartment(null);
        }

        if (employee.getPosition() != null) {
            employee.setPosition(null);
        }

        employeeRepository.delete(employee);
    }

    @Override
    @Transactional
    public boolean toggleEmployeeStatus(UUID id) {
        Employee employee =
                employeeRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
        employee.setActive(!employee.isActive());
        employee.setUpdatedAt(new Date());
        employeeRepository.save(employee);
        return employee.isActive();
    }

    @Override
    public List<EmployeeDTO> getEmployeesByPositionType(PositionType positionType) {
        return employeeRepository.findByPositionType(positionType).stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<EmployeeDTO> searchEmployees(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return employeeRepository.searchEmployees(keyword, pageRequest).map(employeeMapper::toDTO);
    }

    @Override
    public long countEmployeesByStatus(boolean isActive) {
        return employeeRepository.countByIsActive(isActive);
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
