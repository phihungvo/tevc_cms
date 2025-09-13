package carevn.luv2code.cms.tevc_cms_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import carevn.luv2code.cms.tevc_cms_api.dto.TimesheetDTO;
import carevn.luv2code.cms.tevc_cms_api.service.TimesheetService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/timesheets")
@RequiredArgsConstructor
public class TimesheetController {
    private final TimesheetService timesheetService;

    @PostMapping
    public ResponseEntity<TimesheetDTO> createTimesheet(@RequestBody TimesheetDTO timesheetDTO) {
        return ResponseEntity.ok(timesheetService.createTimesheet(timesheetDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TimesheetDTO> updateTimesheet(
            @PathVariable Integer id, @RequestBody TimesheetDTO timesheetDTO) {
        return ResponseEntity.ok(timesheetService.updateTimesheet(id, timesheetDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimesheetDTO> getTimesheet(@PathVariable Integer id) {
        return ResponseEntity.ok(timesheetService.getTimesheet(id));
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<TimesheetDTO> approveTimesheet(
            @PathVariable Integer id, @RequestParam Integer approverId, @RequestParam String comments) {
        return ResponseEntity.ok(timesheetService.approveTimesheet(id, approverId, comments));
    }

    // ... other endpoints
}
