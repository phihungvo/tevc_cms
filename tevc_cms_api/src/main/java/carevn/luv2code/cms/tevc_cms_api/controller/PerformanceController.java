package carevn.luv2code.cms.tevc_cms_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<PerformanceDTO> createPerformance(@RequestBody PerformanceDTO performanceDTO) {
        return ResponseEntity.ok(performanceService.createPerformance(performanceDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerformanceDTO> updatePerformance(
            @PathVariable Integer id, @RequestBody PerformanceDTO performanceDTO) {
        return ResponseEntity.ok(performanceService.updatePerformance(id, performanceDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerformanceDTO> getPerformance(@PathVariable Integer id) {
        return ResponseEntity.ok(performanceService.getPerformance(id));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<PerformanceDTO>> getEmployeePerformances(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(performanceService.getEmployeePerformances(employeeId));
    }

    @GetMapping("/reviewer/{reviewerId}")
    public ResponseEntity<List<PerformanceDTO>> getReviewerPerformances(@PathVariable Integer reviewerId) {
        return ResponseEntity.ok(performanceService.getReviewerPerformances(reviewerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerformance(@PathVariable Integer id) {
        performanceService.deletePerformance(id);
        return ResponseEntity.ok().build();
    }
}
