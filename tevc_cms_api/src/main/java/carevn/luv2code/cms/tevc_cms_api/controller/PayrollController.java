package carevn.luv2code.cms.tevc_cms_api.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import carevn.luv2code.cms.tevc_cms_api.dto.PayrollDTO;
import carevn.luv2code.cms.tevc_cms_api.service.PayrollService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payrolls")
@RequiredArgsConstructor
public class PayrollController {
    private final PayrollService payrollService;

    @PostMapping("/calculate/{employeeId}")
    //    @PreAuthorize("hasAuthority('PAYROLL:CREATE')")
    public ResponseEntity<PayrollDTO> calculatePayroll(@PathVariable Integer employeeId, @RequestParam String period) {
        return ResponseEntity.ok(payrollService.calculatePayroll(employeeId, period));
    }

    @PatchMapping("/process")
    //    @PreAuthorize("hasAuthority('PAYROLL:PROCESS')")
    public ResponseEntity<List<PayrollDTO>> processPayroll(@RequestBody List<Integer> payrollIds) {
        List<PayrollDTO> processedPayrolls =
                payrollIds.stream().map(payrollService::processPayroll).toList();
        return ResponseEntity.ok(processedPayrolls);
    }

    @PatchMapping("/{id}/finalize")
    @PreAuthorize("hasAuthority('PAYROLL:FINALIZE')")
    public ResponseEntity<PayrollDTO> finalizePayroll(@PathVariable Integer id) {
        return ResponseEntity.ok(payrollService.finalizePayroll(id));
    }

    @GetMapping
    //    @PreAuthorize("hasAuthority('PAYROLL:READ')")
    public ResponseEntity<Page<PayrollDTO>> getAllPayrolls(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(payrollService.getAllPayrolls(page, size));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PayrollDTO> updatePayroll(@PathVariable Integer id, @RequestBody PayrollDTO payrollDTO) {
        return ResponseEntity.ok(payrollService.updatePayroll(id, payrollDTO));
    }
}
