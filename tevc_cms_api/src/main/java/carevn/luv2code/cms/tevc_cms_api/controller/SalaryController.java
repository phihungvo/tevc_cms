package carevn.luv2code.cms.tevc_cms_api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import carevn.luv2code.cms.tevc_cms_api.dto.SalaryDTO;
import carevn.luv2code.cms.tevc_cms_api.service.SalaryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/salaries")
@RequiredArgsConstructor
public class SalaryController {
    private final SalaryService salaryService;

    @PostMapping
    public SalaryDTO createSalary(@RequestBody SalaryDTO salaryDTO) {
        return salaryService.createSalary(salaryDTO);
    }

    @GetMapping
    public ResponseEntity<Page<SalaryDTO>> getAllSalaries(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(salaryService.getAllSalaries(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalaryDTO> getSalary(@PathVariable UUID id) {
        return ResponseEntity.ok(salaryService.getSalary(id));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<SalaryDTO>> getEmployeeSalaries(@PathVariable UUID employeeId) {
        return ResponseEntity.ok(salaryService.getEmployeeSalaries(employeeId));
    }

    @PostMapping("/calculate/{employeeId}")
    public ResponseEntity<SalaryDTO> calculateSalary(@PathVariable UUID employeeId, @RequestParam String period) {
        return ResponseEntity.ok(salaryService.calculateSalary(employeeId, period));
    }
}
