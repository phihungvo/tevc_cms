package carevn.luv2code.cms.tevc_cms_api.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import carevn.luv2code.cms.tevc_cms_api.dto.LeaveDTO;
import carevn.luv2code.cms.tevc_cms_api.service.LeaveService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveController {
    private final LeaveService leaveService;

    @PostMapping
    public ResponseEntity<LeaveDTO> createLeave(@RequestBody LeaveDTO leaveDTO) {
        return ResponseEntity.ok(leaveService.createLeave(leaveDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<LeaveDTO> updateLeave(@PathVariable Integer id, @RequestBody LeaveDTO leaveDTO) {
        return ResponseEntity.ok(leaveService.updateLeave(id, leaveDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveDTO> getLeave(@PathVariable Integer id) {
        return ResponseEntity.ok(leaveService.getLeave(id));
    }

    @GetMapping
    public ResponseEntity<Page<LeaveDTO>> getAllLeaves(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(leaveService.getAllLeaves(page, size));
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('LEAVE:APPROVE')")
    public ResponseEntity<LeaveDTO> approveLeave(@PathVariable Integer id, @RequestParam String comments) {
        return ResponseEntity.ok(leaveService.approveLeave(id, comments));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<LeaveDTO> rejectLeave(@PathVariable Integer id, @RequestParam String comments) {
        return ResponseEntity.ok(leaveService.rejectLeave(id, comments));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeave(@PathVariable Integer id) {
        leaveService.deleteLeave(id);
        return ResponseEntity.ok().build();
    }
}
