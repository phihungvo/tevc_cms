package carevn.luv2code.cms.tevc_cms_api.controller;

import carevn.luv2code.cms.tevc_cms_api.dto.TimesheetDTO;
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
import carevn.luv2code.cms.tevc_cms_api.service.TimesheetService;
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
@RequestMapping("/api/timesheets")
@RequiredArgsConstructor
public class TimesheetController {
    private final TimesheetService timesheetService;

    @PostMapping
    @PreAuthorize("hasAuthority('TIMESHEET:CREATE')")
    public ResponseEntity<TimesheetDTO> createTimesheet(@RequestBody TimesheetDTO timesheetDTO) {
        return ResponseEntity.ok(timesheetService.createTimesheet(timesheetDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('TIMESHEET:UPDATE')")
    public ResponseEntity<TimesheetDTO> updateTimesheet(
            @PathVariable UUID id,
            @RequestBody TimesheetDTO timesheetDTO) {
        return ResponseEntity.ok(timesheetService.updateTimesheet(id, timesheetDTO));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('TIMESHEET:READ')")
    public ResponseEntity<TimesheetDTO> getTimesheet(@PathVariable UUID id) {
        return ResponseEntity.ok(timesheetService.getTimesheet(id));
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('TIMESHEET:APPROVE')")
    public ResponseEntity<TimesheetDTO> approveTimesheet(
            @PathVariable UUID id,
            @RequestParam UUID approverId,
            @RequestParam String comments) {
        return ResponseEntity.ok(timesheetService.approveTimesheet(id, approverId, comments));
    }

    // ... other endpoints
}
