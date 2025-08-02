package carevn.luv2code.cms.tevc_cms_api.controller;

import carevn.luv2code.cms.tevc_cms_api.dto.PayrollDTO;
import carevn.luv2code.cms.tevc_cms_api.dto.requests.AuthenticationRequest;
import carevn.luv2code.cms.tevc_cms_api.dto.requests.LogoutRequest;
import carevn.luv2code.cms.tevc_cms_api.dto.requests.RegisterRequest;
import carevn.luv2code.cms.tevc_cms_api.dto.response.AuthenticationResponse;
import carevn.luv2code.cms.tevc_cms_api.dto.response.LogoutResponse;
import carevn.luv2code.cms.tevc_cms_api.entity.User;
import carevn.luv2code.cms.tevc_cms_api.repository.UserRepository;
import carevn.luv2code.cms.tevc_cms_api.security.AuthService;
import carevn.luv2code.cms.tevc_cms_api.security.JwtService;
import carevn.luv2code.cms.tevc_cms_api.security.TokenBlacklist;
import carevn.luv2code.cms.tevc_cms_api.service.PayrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/payrolls")
@RequiredArgsConstructor
public class PayrollController {
    private final PayrollService payrollService;

    @PostMapping("/calculate/{employeeId}")
    @PreAuthorize("hasAuthority('PAYROLL:CREATE')")
    public ResponseEntity<PayrollDTO> calculatePayroll(
            @PathVariable UUID employeeId,
            @RequestParam String period) {
        return ResponseEntity.ok(payrollService.calculatePayroll(employeeId, period));
    }

    @PatchMapping("/{id}/process")
    @PreAuthorize("hasAuthority('PAYROLL:PROCESS')")
    public ResponseEntity<PayrollDTO> processPayroll(@PathVariable UUID id) {
        return ResponseEntity.ok(payrollService.processPayroll(id));
    }

    @PatchMapping("/{id}/finalize")
    @PreAuthorize("hasAuthority('PAYROLL:FINALIZE')")
    public ResponseEntity<PayrollDTO> finalizePayroll(@PathVariable UUID id) {
        return ResponseEntity.ok(payrollService.finalizePayroll(id));
    }

    // ... other endpoints
}
