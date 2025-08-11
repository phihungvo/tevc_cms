package carevn.luv2code.cms.tevc_cms_api.service;

import carevn.luv2code.cms.tevc_cms_api.dto.DepartmentDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface DepartmentService {
    DepartmentDTO createDepartment(DepartmentDTO departmentDTO);

    DepartmentDTO updateDepartment(UUID id, DepartmentDTO departmentDTO);

    void deleteDepartment(UUID id);

    DepartmentDTO getDepartment(UUID id);

    Page<DepartmentDTO> getAllDepartments(int page, int size);

    List<DepartmentDTO> getAllDepartmentsNoPaging();

    DepartmentDTO assignManager(UUID departmentId, UUID managerId);

}
