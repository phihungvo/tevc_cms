package carevn.luv2code.cms.tevc_cms_api.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import carevn.luv2code.cms.tevc_cms_api.dto.CandidateDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Candidate;
import carevn.luv2code.cms.tevc_cms_api.entity.JobPosting;
import carevn.luv2code.cms.tevc_cms_api.enums.CandidateStatus;
import carevn.luv2code.cms.tevc_cms_api.exception.AppException;
import carevn.luv2code.cms.tevc_cms_api.exception.ErrorCode;
import carevn.luv2code.cms.tevc_cms_api.mapper.CandidateMapper;
import carevn.luv2code.cms.tevc_cms_api.repository.CandidateRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.JobPostingRepository;
import carevn.luv2code.cms.tevc_cms_api.service.CandidateService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;
    private final JobPostingRepository jobPostingRepository;
    private final CandidateMapper candidateMapper;

    @Override
    @Transactional
    public void applyForJob(Integer candidateId, Integer jobPostingId) {
        Candidate candidate = candidateRepository
                .findById(candidateId)
                .orElseThrow(() -> new AppException(ErrorCode.CANDIDATE_NOT_FOUND));

        JobPosting jobPosting = jobPostingRepository
                .findById(jobPostingId)
                .orElseThrow(() -> new AppException(ErrorCode.JOB_POSTING_NOT_FOUND));

        if (candidate.getJobPostings().contains(jobPosting)) {
            throw new AppException(ErrorCode.CANDIDATE_ALREADY_APPLIED);
        }

        candidate.getJobPostings().add(jobPosting);
        candidateRepository.save(candidate);
    }

    @Override
    @Transactional
    public void updateStatus(Integer candidateId, CandidateStatus status) {
        Candidate candidate = candidateRepository
                .findById(candidateId)
                .orElseThrow(() -> new AppException(ErrorCode.CANDIDATE_NOT_FOUND));

        candidate.setStatus(status);
        candidateRepository.save(candidate);
    }

    @Override
    public String uploadResume(String resumeFilePath) throws Exception {
        // Placeholder for file upload logic
        return "Uploaded file path: " + resumeFilePath;
    }

    @Override
    public void sendNotification(String subject, String content) {
        // Placeholder for notification logic
    }

    @Override
    @Transactional
    public void scheduleInterview(Integer candidateId, LocalDateTime interviewDate) {
        Candidate candidate = candidateRepository
                .findById(candidateId)
                .orElseThrow(() -> new AppException(ErrorCode.CANDIDATE_NOT_FOUND));
        // Logic to schedule interview
    }

    @Override
    @Transactional
    public void rejectCandidate(Integer candidateId, String reason) {
        Candidate candidate = candidateRepository
                .findById(candidateId)
                .orElseThrow(() -> new AppException(ErrorCode.CANDIDATE_NOT_FOUND));
        candidate.setStatus(CandidateStatus.REJECTED);
        candidateRepository.save(candidate);
    }

    @Override
    @Transactional
    public void hireCandidate(Integer candidateId) {
        Candidate candidate = candidateRepository
                .findById(candidateId)
                .orElseThrow(() -> new AppException(ErrorCode.CANDIDATE_NOT_FOUND));
        candidate.setStatus(CandidateStatus.HIRED);
        candidateRepository.save(candidate);

        // Send email notification logic can be added here
    }

    @Override
    public CandidateDTO getCandidate(Integer candidateId) {
        Candidate candidate = candidateRepository
                .findById(candidateId)
                .orElseThrow(() -> new AppException(ErrorCode.CANDIDATE_NOT_FOUND));
        return candidateMapper.toDTO(candidate);
    }

    @Override
    public List<CandidateDTO> getAllCandidates() {
        return candidateRepository.findAll().stream()
                .map(candidateMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateCandidate(Integer candidateId, CandidateDTO candidateDTO) {
        Candidate candidate = candidateRepository
                .findById(candidateId)
                .orElseThrow(() -> new AppException(ErrorCode.CANDIDATE_NOT_FOUND));
        candidateMapper.updateCandidateFromDto(candidateDTO, candidate);
        candidateRepository.save(candidate);
    }

    @Override
    @Transactional
    public void deleteCandidate(Integer candidateId) {
        Candidate candidate = candidateRepository
                .findById(candidateId)
                .orElseThrow(() -> new AppException(ErrorCode.CANDIDATE_NOT_FOUND));
        candidateRepository.delete(candidate);
    }

    @Override
    public List<CandidateDTO> searchCandidates(String keyword) {
        // Placeholder for search logic
        return List.of();
    }

    @Override
    public Page<CandidateDTO> getCandidatesWithPagination(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return candidateRepository.findAll(pageRequest).map(candidateMapper::toDTO);
    }
}
