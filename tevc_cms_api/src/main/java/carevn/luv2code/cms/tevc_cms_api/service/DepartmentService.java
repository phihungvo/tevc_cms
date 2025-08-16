package carevn.luv2code.cms.tevc_cms_api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import carevn.luv2code.cms.tevc_cms_api.dto.DepartmentDTO;

public interface DepartmentService {
    DepartmentDTO createDepartment(DepartmentDTO departmentDTO);

    DepartmentDTO updateDepartment(UUID id, DepartmentDTO departmentDTO);

    void deleteDepartment(UUID id);

    DepartmentDTO getDepartment(UUID id);

    Page<DepartmentDTO> getAllDepartments(int page, int size);

    List<DepartmentDTO> getAllDepartmentsNoPaging();

    DepartmentDTO assignManager(UUID departmentId, UUID managerId);
}
