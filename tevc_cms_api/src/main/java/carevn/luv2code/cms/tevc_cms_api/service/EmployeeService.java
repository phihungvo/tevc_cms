package carevn.luv2code.cms.tevc_cms_api.service;

import java.util.List;

import org.springframework.data.domain.Page;

import carevn.luv2code.cms.tevc_cms_api.dto.EmployeeDTO;
import carevn.luv2code.cms.tevc_cms_api.enums.PositionType;
import carevn.luv2code.cms.tevc_cms_api.repository.EmployeeRepository;

public interface EmployeeService {

    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);

    EmployeeDTO updateEmployee(Integer id, EmployeeDTO employeeDTO);

    void deleteEmployee(Integer id);

    EmployeeDTO getEmployee(Integer id);

    Page<EmployeeDTO> getAllEmployees(int page, int size);

    List<EmployeeDTO> getAllEmployeesNoPaging();

    Page<EmployeeDTO> findByDepartment(Integer departmentId, int page, int size);

    List<EmployeeDTO> getEmployeesByDepartmentId(Integer departmentId);

    List<EmployeeRepository.EmployeeBasicProjection> getBasicEmployeesByDepartmentId(Integer departmentId);

    boolean toggleEmployeeStatus(Integer id);

    List<EmployeeDTO> getEmployeesByPositionType(PositionType positionType);

    Page<EmployeeDTO> searchEmployees(String keyword, int page, int size);

    long countEmployeesByStatus(boolean isActive);
}
