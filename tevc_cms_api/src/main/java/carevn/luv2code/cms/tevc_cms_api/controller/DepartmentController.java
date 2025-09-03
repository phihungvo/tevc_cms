package carevn.luv2code.cms.tevc_cms_api.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import carevn.luv2code.cms.tevc_cms_api.dto.DepartmentDTO;
import carevn.luv2code.cms.tevc_cms_api.service.DepartmentService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @PostMapping
    //    @PreAuthorize("hasAuthority('DEPARTMENT:CREATE')")
    public ResponseEntity<DepartmentDTO> createDepartment(@RequestBody DepartmentDTO departmentDTO) {
        return ResponseEntity.ok(departmentService.createDepartment(departmentDTO));
    }

    @PatchMapping("/{id}")
    //    @PreAuthorize("hasAuthority('DEPARTMENT:UPDATE')")
    public ResponseEntity<DepartmentDTO> updateDepartment(
            @PathVariable Integer id, @RequestBody DepartmentDTO departmentDTO) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, departmentDTO));
    }

    @GetMapping("/{id}")
    //    @PreAuthorize("hasAuthority('DEPARTMENT:READ')")
    public ResponseEntity<DepartmentDTO> getDepartment(@PathVariable Integer id) {
        return ResponseEntity.ok(departmentService.getDepartment(id));
    }

    @GetMapping
    //    @PreAuthorize("hasAuthority('DEPARTMENT:READ')")
    public ResponseEntity<Page<DepartmentDTO>> getAllDepartments(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(departmentService.getAllDepartments(page, size));
    }

    @GetMapping("/no-paging")
    //    @PreAuthorize("hasAuthority('DEPARTMENT:READ')")
    public ResponseEntity<List<DepartmentDTO>> getAllDepartmentsNoPaging() {
        return ResponseEntity.ok(departmentService.getAllDepartmentsNoPaging());
    }

    @DeleteMapping("/{id}")
    //    @PreAuthorize("hasAuthority('DEPARTMENT:DELETE')")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Integer id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/manager/{managerId}")
    //    @PreAuthorize("hasAuthority('DEPARTMENT:UPDATE')")
    public ResponseEntity<DepartmentDTO> assignManager(@PathVariable Integer id, @PathVariable Integer managerId) {
        return ResponseEntity.ok(departmentService.assignManager(id, managerId));
    }
}
