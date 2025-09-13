package carevn.luv2code.cms.tevc_cms_api.service;

import java.util.List;

import org.springframework.data.domain.Page;

import carevn.luv2code.cms.tevc_cms_api.dto.DepartmentDTO;

public interface DepartmentService {
    DepartmentDTO createDepartment(DepartmentDTO departmentDTO);

    DepartmentDTO updateDepartment(Integer id, DepartmentDTO departmentDTO);

    void deleteDepartment(Integer id);

    DepartmentDTO getDepartment(Integer id);

    Page<DepartmentDTO> getAllDepartments(int page, int size);

    List<DepartmentDTO> getAllDepartmentsNoPaging();

    DepartmentDTO assignManager(Integer departmentId, Integer managerId);
}
