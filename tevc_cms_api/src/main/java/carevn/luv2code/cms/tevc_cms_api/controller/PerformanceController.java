package carevn.luv2code.cms.tevc_cms_api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import carevn.luv2code.cms.tevc_cms_api.dto.PerformanceDTO;
import carevn.luv2code.cms.tevc_cms_api.service.PerformanceService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/performances")
@RequiredArgsConstructor
public class PerformanceController {
    private final PerformanceService performanceService;

    @PostMapping
    @PreAuthorize("hasAuthority('PERFORMANCE:CREATE')")
    public ResponseEntity<PerformanceDTO> createPerformance(@RequestBody PerformanceDTO performanceDTO) {
        return ResponseEntity.ok(performanceService.createPerformance(performanceDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PERFORMANCE:UPDATE')")
    public ResponseEntity<PerformanceDTO> updatePerformance(
            @PathVariable UUID id, @RequestBody PerformanceDTO performanceDTO) {
        return ResponseEntity.ok(performanceService.updatePerformance(id, performanceDTO));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PERFORMANCE:READ')")
    public ResponseEntity<PerformanceDTO> getPerformance(@PathVariable UUID id) {
        return ResponseEntity.ok(performanceService.getPerformance(id));
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAuthority('PERFORMANCE:READ')")
    public ResponseEntity<List<PerformanceDTO>> getEmployeePerformances(@PathVariable UUID employeeId) {
        return ResponseEntity.ok(performanceService.getEmployeePerformances(employeeId));
    }

    @GetMapping("/reviewer/{reviewerId}")
    @PreAuthorize("hasAuthority('PERFORMANCE:READ')")
    public ResponseEntity<List<PerformanceDTO>> getReviewerPerformances(@PathVariable UUID reviewerId) {
        return ResponseEntity.ok(performanceService.getReviewerPerformances(reviewerId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PERFORMANCE:DELETE')")
    public ResponseEntity<Void> deletePerformance(@PathVariable UUID id) {
        performanceService.deletePerformance(id);
        return ResponseEntity.ok().build();
    }
}
