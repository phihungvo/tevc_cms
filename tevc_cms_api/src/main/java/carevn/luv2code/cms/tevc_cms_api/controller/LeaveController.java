package carevn.luv2code.cms.tevc_cms_api.controller;

import carevn.luv2code.cms.tevc_cms_api.dto.LeaveDTO;
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
import carevn.luv2code.cms.tevc_cms_api.service.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveController {
    private final LeaveService leaveService;

    @PostMapping
//    @PreAuthorize("hasAuthority('LEAVE:CREATE')")
    public ResponseEntity<LeaveDTO> createLeave(@RequestBody LeaveDTO leaveDTO) {
        return ResponseEntity.ok(leaveService.createLeave(leaveDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('LEAVE:UPDATE')")
    public ResponseEntity<LeaveDTO> updateLeave(
            @PathVariable UUID id,
            @RequestBody LeaveDTO leaveDTO) {
        return ResponseEntity.ok(leaveService.updateLeave(id, leaveDTO));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('LEAVE:READ')")
    public ResponseEntity<LeaveDTO> getLeave(@PathVariable UUID id) {
        return ResponseEntity.ok(leaveService.getLeave(id));
    }

    @GetMapping
//    @PreAuthorize("hasAuthority('LEAVE:READ')")
    public ResponseEntity<Page<LeaveDTO>> getAllLeaves(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(leaveService.getAllLeaves(page, size));
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('LEAVE:APPROVE')")
    public ResponseEntity<LeaveDTO> approveLeave(
            @PathVariable UUID id,
            @RequestParam String comments) {
        return ResponseEntity.ok(leaveService.approveLeave(id, comments));
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('LEAVE:APPROVE')")
    public ResponseEntity<LeaveDTO> rejectLeave(
            @PathVariable UUID id,
            @RequestParam String comments) {
        return ResponseEntity.ok(leaveService.rejectLeave(id, comments));
    }
}
