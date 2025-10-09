package carevn.luv2code.cms.tevc_cms_api.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
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

    @GetMapping("/paginated")
    public ResponseEntity<Page<InterviewDTO>> getInterviewsPaginated(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<InterviewDTO> interviewsPaginated = interviewService.getInterviewsPaginated(page, size);
        return ResponseEntity.ok(interviewsPaginated);
    }

    //    @GetMapping("/candidate/{candidateId}")
    //    public ResponseEntity<List<InterviewDTO>> getInterviewsByCandidate(@PathVariable Integer candidateId) {
    //        return ResponseEntity.ok(interviewService.getInterviewsByCandidate(candidateId));
    //    }

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<Page<InterviewDTO>> getInterviewsByCandidate(
            @PathVariable Integer candidateId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(interviewService.getInterviewsByCandidate(candidateId, page, size));
    }

    @GetMapping("/by-candidates")
    public ResponseEntity<Page<InterviewDTO>> getInterviewsByCandidates(
            @RequestParam List<Integer> candidateIds,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<InterviewDTO> result = interviewService.getInterviewsByCandidates(candidateIds, page, size);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/by-job-posting/{jobPostingId}")
    public ResponseEntity<Page<InterviewDTO>> getInterviewsByJobPosting(
            @PathVariable Integer jobPostingId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<InterviewDTO> interviews = interviewService.getInterviewsByJobPosting(jobPostingId, page, size);
        return ResponseEntity.ok(interviews);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<InterviewDTO>> getInterviewsByDateRange(
            @RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(interviewService.getInterviewsByDateRange(startDate, endDate));
    }
}
