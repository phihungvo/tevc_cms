package carevn.luv2code.cms.tevc_cms_api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import carevn.luv2code.cms.tevc_cms_api.dto.CandidateDTO;
import carevn.luv2code.cms.tevc_cms_api.enums.CandidateStatus;

public interface CandidateService {
    void applyForJob(UUID candidateId, UUID jobPostingId);

    void updateStatus(UUID candidateId, CandidateStatus status);

    String uploadResume(String resumeFilePath) throws Exception;

    void sendNotification(String subject, String content);

    void scheduleInterview(UUID candidateId, LocalDateTime interviewDate);

    void rejectCandidate(UUID candidateId, String reason);

    void hireCandidate(UUID candidateId);

    CandidateDTO getCandidate(UUID candidateId);

    List<CandidateDTO> getAllCandidates();

    void updateCandidate(UUID candidateId, CandidateDTO candidateDTO);

    void deleteCandidate(UUID candidateId);

    List<CandidateDTO> searchCandidates(String keyword);

    Page<CandidateDTO> getCandidatesWithPagination(int page, int size);
}
