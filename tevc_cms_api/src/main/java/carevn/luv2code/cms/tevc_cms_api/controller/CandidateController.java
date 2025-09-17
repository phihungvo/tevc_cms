package carevn.luv2code.cms.tevc_cms_api.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import carevn.luv2code.cms.tevc_cms_api.dto.CandidateDTO;
import carevn.luv2code.cms.tevc_cms_api.enums.CandidateStatus;
import carevn.luv2code.cms.tevc_cms_api.service.CandidateService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    @PostMapping("/{candidateId}/apply/{jobPostingId}")
    public ResponseEntity<String> applyForJob(@PathVariable Integer candidateId, @PathVariable Integer jobPostingId) {
        candidateService.applyForJob(candidateId, jobPostingId);
        return ResponseEntity.ok("Candidate successfully applied for the job posting.");
    }

    @PutMapping("/{candidateId}/status")
    public ResponseEntity<String> updateStatus(
            @PathVariable Integer candidateId, @RequestParam CandidateStatus status) {
        candidateService.updateStatus(candidateId, status);
        return ResponseEntity.ok("Candidate status updated successfully.");
    }

    @PostMapping("/upload-resume")
    public ResponseEntity<String> uploadResume(@RequestParam String resumeFilePath) throws Exception {
        String uploadedPath = candidateService.uploadResume(resumeFilePath);
        return ResponseEntity.ok("Resume uploaded successfully. Path: " + uploadedPath);
    }

    @PostMapping("/notify")
    public ResponseEntity<String> sendNotification(@RequestParam String subject, @RequestParam String content) {
        candidateService.sendNotification(subject, content);
        return ResponseEntity.ok("Notification sent successfully.");
    }

    @PostMapping("/{id}/schedule-interview")
    public ResponseEntity<String> scheduleInterview(
            @PathVariable Integer id, @RequestParam LocalDateTime interviewDate) {
        candidateService.scheduleInterview(id, interviewDate);
        return ResponseEntity.ok("Interview scheduled successfully for the candidate.");
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<String> rejectCandidate(@PathVariable Integer id, @RequestParam String reason) {
        candidateService.rejectCandidate(id, reason);
        return ResponseEntity.ok("Candidate rejected successfully. Reason: " + reason);
    }

    @PutMapping("/{id}/hire")
    public ResponseEntity<String> hireCandidate(@PathVariable Integer id) {
        candidateService.hireCandidate(id);
        return ResponseEntity.ok("Candidate hired successfully.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidateDTO> getCandidate(@PathVariable Integer id) {
        CandidateDTO candidate = candidateService.getCandidate(id);
        return ResponseEntity.ok(candidate);
    }

    @GetMapping
    public ResponseEntity<List<CandidateDTO>> getAllCandidates() {
        List<CandidateDTO> candidates = candidateService.getAllCandidates();
        return ResponseEntity.ok(candidates);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCandidate(@PathVariable Integer id, @RequestBody CandidateDTO candidateDTO) {
        candidateService.updateCandidate(id, candidateDTO);
        return ResponseEntity.ok("Candidate updated successfully.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCandidate(@PathVariable Integer id) {
        candidateService.deleteCandidate(id);
        return ResponseEntity.ok("Candidate deleted successfully.");
    }

    @GetMapping("/search")
    public ResponseEntity<List<CandidateDTO>> searchCandidates(@RequestParam String keyword) {
        List<CandidateDTO> candidates = candidateService.searchCandidates(keyword);
        return ResponseEntity.ok(candidates);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<CandidateDTO>> getCandidatesWithPagination(
            @RequestParam int page, @RequestParam int size) {
        Page<CandidateDTO> candidates = candidateService.getCandidatesWithPagination(page, size);
        return ResponseEntity.ok(candidates);
    }
}
