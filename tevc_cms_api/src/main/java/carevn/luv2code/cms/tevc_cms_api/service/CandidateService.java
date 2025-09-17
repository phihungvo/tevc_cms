package carevn.luv2code.cms.tevc_cms_api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;

import carevn.luv2code.cms.tevc_cms_api.dto.CandidateDTO;
import carevn.luv2code.cms.tevc_cms_api.enums.CandidateStatus;

public interface CandidateService {
    void applyForJob(Integer candidateId, Integer jobPostingId);

    void updateStatus(Integer candidateId, CandidateStatus status);

    String uploadResume(String resumeFilePath) throws Exception;

    void sendNotification(String subject, String content);

    void scheduleInterview(Integer candidateId, LocalDateTime interviewDate);

    void rejectCandidate(Integer candidateId, String reason);

    void hireCandidate(Integer candidateId);

    CandidateDTO getCandidate(Integer candidateId);

    List<CandidateDTO> getAllCandidates();

    void updateCandidate(Integer candidateId, CandidateDTO candidateDTO);

    void deleteCandidate(Integer candidateId);

    List<CandidateDTO> searchCandidates(String keyword);

    Page<CandidateDTO> getCandidatesWithPagination(int page, int size);
}
