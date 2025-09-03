package carevn.luv2code.cms.tevc_cms_api.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.createEmployee(employeeDTO));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Integer id, @RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employeeDTO));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable Integer id) {
        return ResponseEntity.ok(employeeService.getEmployee(id));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<EmployeeDTO>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(employeeService.getAllEmployees(page, size));
    }

    @GetMapping("/no-paging")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployeesNoPaging() {
        return ResponseEntity.ok(employeeService.getAllEmployeesNoPaging());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Integer id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/toggle-status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> toggleEmployeeStatus(@PathVariable Integer id) {
        return ResponseEntity.ok(employeeService.toggleEmployeeStatus(id));
    }

    @GetMapping("/by-position-type")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByPositionType(@RequestParam String positionType) {
        PositionType type = PositionType.valueOf(positionType.toUpperCase());
        return ResponseEntity.ok(employeeService.getEmployeesByPositionType(type));
    }

    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<EmployeeDTO>> searchEmployees(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(employeeService.searchEmployees(keyword, page, size));
    }

    @GetMapping("/by-department")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<EmployeeDTO>> getEmployeesByDepartment(
            @RequestParam Integer departmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(employeeService.findByDepartment(departmentId, page, size));
    }

    @GetMapping("/stats/by-status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> countEmployeesByStatus(@RequestParam boolean isActive) {
        return ResponseEntity.ok(employeeService.countEmployeesByStatus(isActive));
    }
}
