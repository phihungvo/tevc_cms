package carevn.luv2code.cms.tevc_cms_api.service;

import carevn.luv2code.cms.tevc_cms_api.dto.EmployeeDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Employee;
import carevn.luv2code.cms.tevc_cms_api.enums.PositionType;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {

    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);

    EmployeeDTO updateEmployee(UUID id, EmployeeDTO employeeDTO);

    void deleteEmployee(UUID id);

    EmployeeDTO getEmployee(UUID id);

    Page<EmployeeDTO> getAllEmployees(int page, int size);

//    Page<EmployeeDTO> findByDepartment(UUID departmentId, int page, int size);

    boolean toggleEmployeeStatus(UUID id);

    List<EmployeeDTO> getEmployeesByPositionType(PositionType positionType);

}
