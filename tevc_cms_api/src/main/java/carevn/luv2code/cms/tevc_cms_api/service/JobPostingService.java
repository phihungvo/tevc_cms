package carevn.luv2code.cms.tevc_cms_api.service;

import java.util.List;

import org.springframework.data.domain.Page;

import carevn.luv2code.cms.tevc_cms_api.dto.JobPostingDTO;

public interface JobPostingService {
    JobPostingDTO create(JobPostingDTO dto);

    JobPostingDTO update(Integer id, JobPostingDTO dto);

    void delete(Integer id);

    JobPostingDTO getById(Integer id);

    public Page<JobPostingDTO> getJobPostingPaginated(int page, int size);

    List<JobPostingDTO> getJobPostingsByCandidate(Integer candidateId);
}
