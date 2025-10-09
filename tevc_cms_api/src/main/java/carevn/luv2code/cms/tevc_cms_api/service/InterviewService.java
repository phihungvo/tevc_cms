package carevn.luv2code.cms.tevc_cms_api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;

import carevn.luv2code.cms.tevc_cms_api.dto.InterviewDTO;

public interface InterviewService {

    void createInterview(InterviewDTO interviewDTO);

    void updateInterview(Integer interviewId, LocalDateTime newDate, String newInterviewer);

    void cancelInterview(Integer interviewId);

    InterviewDTO getInterview(Integer interviewId);

    List<InterviewDTO> getAllInterviews();

    //    List<InterviewDTO> getInterviewsByCandidate(Integer candidateId);

    Page<InterviewDTO> getInterviewsByCandidate(Integer candidateId, int page, int size);

    Page<InterviewDTO> getInterviewsByJobPosting(Integer jobPostingId, int page, int size);

    Page<InterviewDTO> getInterviewsByCandidates(List<Integer> candidateIds, int page, int size);

    List<InterviewDTO> getInterviewsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    Page<InterviewDTO> getInterviewsPaginated(int page, int size);
}
