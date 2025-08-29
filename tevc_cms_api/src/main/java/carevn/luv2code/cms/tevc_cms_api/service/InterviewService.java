package carevn.luv2code.cms.tevc_cms_api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import carevn.luv2code.cms.tevc_cms_api.dto.InterviewDTO;

public interface InterviewService {

    void createInterview(InterviewDTO interviewDTO);

    void updateInterview(UUID interviewId, LocalDateTime newDate, String newInterviewer);

    void cancelInterview(UUID interviewId);

    InterviewDTO getInterview(UUID interviewId);

    List<InterviewDTO> getAllInterviews();

    List<InterviewDTO> getInterviewsByCandidate(UUID candidateId);

    List<InterviewDTO> getInterviewsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
}
