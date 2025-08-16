package carevn.luv2code.cms.tevc_cms_api.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import carevn.luv2code.cms.tevc_cms_api.dto.DepartmentDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Department;
import carevn.luv2code.cms.tevc_cms_api.entity.Employee;
import carevn.luv2code.cms.tevc_cms_api.exception.AppException;
import carevn.luv2code.cms.tevc_cms_api.exception.ErrorCode;
import carevn.luv2code.cms.tevc_cms_api.mapper.DepartmentMapper;
import carevn.luv2code.cms.tevc_cms_api.repository.DepartmentRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.EmployeeRepository;
import carevn.luv2code.cms.tevc_cms_api.service.DepartmentService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    @Transactional
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        if (departmentRepository.existsByName(departmentDTO.getName())) {
            throw new AppException(ErrorCode.DEPARTMENT_NAME_EXISTS);
        }

        Department department = departmentMapper.toEntity(departmentDTO);
        if (departmentDTO.getManagerId() != null) {
            Employee manager = employeeRepository
                    .findById(departmentDTO.getManagerId())
                    .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
            department.setManager(manager);
        }
        Department savedDepartment = departmentRepository.save(department);
        return departmentMapper.toDTO(savedDepartment);
    }

    @Override
    @Transactional
    public DepartmentDTO assignManager(UUID departmentId, UUID managerId) {
        Department department = departmentRepository
                .findById(departmentId)
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));

        Employee manager = employeeRepository
                .findById(managerId)
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

        department.setManager(manager);
        Department savedDepartment = departmentRepository.save(department);
        return departmentMapper.toDTO(savedDepartment);
    }

    @Override
    @Transactional
    public DepartmentDTO updateDepartment(UUID id, DepartmentDTO departmentDTO) {
        Department department =
                departmentRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));

        if (departmentDTO.getName() != null
                && !departmentDTO.getName().equals(department.getName())
                && departmentRepository.existsByName(departmentDTO.getName())) {
            throw new AppException(ErrorCode.DEPARTMENT_NAME_EXISTS);
        }

        if (departmentDTO.getManagerId() != null) {
            Employee manager = employeeRepository
                    .findById(departmentDTO.getManagerId())
                    .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
            department.setManager(manager);
        } else {
            department.setManager(null);
        }

        departmentMapper.updateDepartmentFromDto(departmentDTO, department);
        Department updatedDepartment = departmentRepository.save(department);
        return departmentMapper.toDTO(updatedDepartment);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentDTO getDepartment(UUID id) {
        Department department =
                departmentRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
        return departmentMapper.toDTO(department);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentDTO> getAllDepartments(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return departmentRepository.findAll(pageRequest).map(departmentMapper::toDTO);
    }

    @Override
    public List<DepartmentDTO> getAllDepartmentsNoPaging() {
        List<Department> departmentList = departmentRepository.findAll();
        return departmentList.stream().map(departmentMapper::toDTO).toList();
    }

    @Override
    @Transactional
    public void deleteDepartment(UUID id) {
        Department department =
                departmentRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
        if (!department.getEmployees().isEmpty()) {
            throw new AppException(ErrorCode.DEPARTMENT_HAS_EMPLOYEES);
        }
        department.setManager(null);
        departmentRepository.delete(department);
    }
}
