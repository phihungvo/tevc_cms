package carevn.luv2code.cms.tevc_cms_api.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import carevn.luv2code.cms.tevc_cms_api.dto.InterviewDTO;
import carevn.luv2code.cms.tevc_cms_api.service.InterviewService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping
    public ResponseEntity<String> createInterview(@RequestBody InterviewDTO interviewDTO) {
        interviewService.createInterview(interviewDTO);
        return ResponseEntity.ok("Interview created successfully.");
    }

    @PutMapping("/{interviewId}")
    public ResponseEntity<String> updateInterview(
            @PathVariable Integer interviewId,
            @RequestParam LocalDateTime newDate,
            @RequestParam String newInterviewer) {
        interviewService.updateInterview(interviewId, newDate, newInterviewer);
        return ResponseEntity.ok("Interview updated successfully.");
    }

    @DeleteMapping("/{interviewId}")
    public ResponseEntity<String> cancelInterview(@PathVariable Integer interviewId) {
        interviewService.cancelInterview(interviewId);
        return ResponseEntity.ok("Interview canceled successfully.");
    }

    @GetMapping("/{interviewId}")
    public ResponseEntity<InterviewDTO> getInterview(@PathVariable Integer interviewId) {
        return ResponseEntity.ok(interviewService.getInterview(interviewId));
    }

    @GetMapping
    public ResponseEntity<List<InterviewDTO>> getAllInterviews() {
        return ResponseEntity.ok(interviewService.getAllInterviews());
    }

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<List<InterviewDTO>> getInterviewsByCandidate(@PathVariable Integer candidateId) {
        return ResponseEntity.ok(interviewService.getInterviewsByCandidate(candidateId));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<InterviewDTO>> getInterviewsByDateRange(
            @RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(interviewService.getInterviewsByDateRange(startDate, endDate));
    }
}
