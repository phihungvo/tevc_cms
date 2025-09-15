package carevn.luv2code.cms.tevc_cms_api.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import carevn.luv2code.cms.tevc_cms_api.dto.EmployeeDTO;
import carevn.luv2code.cms.tevc_cms_api.dto.response.ApiResponse;
import carevn.luv2code.cms.tevc_cms_api.enums.PositionType;
import carevn.luv2code.cms.tevc_cms_api.repository.EmployeeRepository;
import carevn.luv2code.cms.tevc_cms_api.service.EmployeeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.createEmployee(employeeDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Integer id, @RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employeeDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable Integer id) {
        return ResponseEntity.ok(employeeService.getEmployee(id));
    }

    @GetMapping
    public ResponseEntity<Page<EmployeeDTO>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(employeeService.getAllEmployees(page, size));
    }

    @GetMapping("/no-paging")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployeesNoPaging() {
        return ResponseEntity.ok(employeeService.getAllEmployeesNoPaging());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Integer id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<Boolean> toggleEmployeeStatus(@PathVariable Integer id) {
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
            @RequestParam Integer departmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(employeeService.findByDepartment(departmentId, page, size));
    }

    @GetMapping("/by-department/no-paging")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByDepartmentId(@RequestParam Integer departmentId) {
        return ResponseEntity.ok(employeeService.getEmployeesByDepartmentId(departmentId));
    }

    @GetMapping("/stats/by-status")
    public ResponseEntity<Long> countEmployeesByStatus(@RequestParam boolean isActive) {
        return ResponseEntity.ok(employeeService.countEmployeesByStatus(isActive));
    }

    @GetMapping("/by-department/basic")
    public ApiResponse<List<EmployeeRepository.EmployeeBasicProjection>> getBasicEmployeesByDepartmentId(
            @RequestParam Integer departmentId) {
        return ApiResponse.<List<EmployeeRepository.EmployeeBasicProjection>>builder()
                .code(HttpStatus.OK.value())
                .message("Get basic employees by department ID successful")
                .result(employeeService.getBasicEmployeesByDepartmentId(departmentId))
                .build();
    }
}
