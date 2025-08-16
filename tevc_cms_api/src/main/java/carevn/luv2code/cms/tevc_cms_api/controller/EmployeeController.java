package carevn.luv2code.cms.tevc_cms_api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import carevn.luv2code.cms.tevc_cms_api.dto.EmployeeDTO;
import carevn.luv2code.cms.tevc_cms_api.enums.PositionType;
import carevn.luv2code.cms.tevc_cms_api.service.EmployeeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    //    @PreAuthorize("hasAuthority('EMPLOYEE:CREATE')")
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.createEmployee(employeeDTO));
    }

    @PatchMapping("/{id}")
    //    @PreAuthorize("hasAuthority('EMPLOYEE:UPDATE')")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable UUID id, @RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employeeDTO));
    }

    @GetMapping("/{id}")
    //    @PreAuthorize("hasAuthority('EMPLOYEE:READ')")
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable UUID id) {
        return ResponseEntity.ok(employeeService.getEmployee(id));
    }

    @GetMapping
    //    @PreAuthorize("hasAuthority('EMPLOYEE:READ')")
    public ResponseEntity<Page<EmployeeDTO>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(employeeService.getAllEmployees(page, size));
    }

    @DeleteMapping("/{id}")
    //    @PreAuthorize("hasAuthority('EMPLOYEE:DELETE')")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/toggle-status")
    //    @PreAuthorize("hasAuthority('EMPLOYEE:UPDATE')")
    public ResponseEntity<Boolean> toggleEmployeeStatus(@PathVariable UUID id) {
        return ResponseEntity.ok(employeeService.toggleEmployeeStatus(id));
    }

    @GetMapping("/by-position-type")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByPositionType(@RequestParam String positionType) {
        PositionType type = PositionType.valueOf(positionType.toUpperCase());
        return ResponseEntity.ok(employeeService.getEmployeesByPositionType(type));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<EmployeeDTO>> searchEmployees(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(employeeService.searchEmployees(keyword, page, size));
    }

    @GetMapping("/by-department")
    public ResponseEntity<Page<EmployeeDTO>> getEmployeesByDepartment(
            @RequestParam UUID departmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(employeeService.findByDepartment(departmentId, page, size));
    }

    @GetMapping("/stats/by-status")
    public ResponseEntity<Long> countEmployeesByStatus(@RequestParam boolean isActive) {
        return ResponseEntity.ok(employeeService.countEmployeesByStatus(isActive));
    }
}
