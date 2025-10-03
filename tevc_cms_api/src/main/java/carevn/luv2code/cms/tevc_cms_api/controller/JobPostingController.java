package carevn.luv2code.cms.tevc_cms_api.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import carevn.luv2code.cms.tevc_cms_api.dto.JobPostingDTO;
import carevn.luv2code.cms.tevc_cms_api.service.JobPostingService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/job-postings")
@RequiredArgsConstructor
public class JobPostingController {

    private final JobPostingService jobPostingService;

    @PostMapping
    public JobPostingDTO create(@RequestBody JobPostingDTO dto) {
        return jobPostingService.create(dto);
    }

    @PutMapping("/{id}")
    public JobPostingDTO update(@PathVariable Integer id, @RequestBody JobPostingDTO dto) {
        return jobPostingService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        jobPostingService.delete(id);
    }

    @GetMapping("/{id}")
    public JobPostingDTO getById(@PathVariable Integer id) {
        return jobPostingService.getById(id);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<JobPostingDTO>> getJobPostingPaginated(@RequestParam int page, @RequestParam int size) {
        Page<JobPostingDTO> jobPostingDTOS = jobPostingService.getJobPostingPaginated(page, size);
        return ResponseEntity.ok(jobPostingDTOS);
    }

    @GetMapping("/{candidateId}/by-candidate")
    public ResponseEntity<List<JobPostingDTO>> getJobPostingsByCandidate(@PathVariable Integer candidateId) {
        List<JobPostingDTO> jobPostings = jobPostingService.getJobPostingsByCandidate(candidateId);
        return ResponseEntity.ok(jobPostings);
    }
}
