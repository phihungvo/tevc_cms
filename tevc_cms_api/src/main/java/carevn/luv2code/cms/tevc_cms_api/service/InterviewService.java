package carevn.luv2code.cms.tevc_cms_api.service;

import java.time.LocalDateTime;
import java.util.List;

import carevn.luv2code.cms.tevc_cms_api.dto.InterviewDTO;

public interface InterviewService {

    void createInterview(InterviewDTO interviewDTO);

    void updateInterview(Integer interviewId, LocalDateTime newDate, String newInterviewer);

    void cancelInterview(Integer interviewId);

    InterviewDTO getInterview(Integer interviewId);

    List<InterviewDTO> getAllInterviews();

    List<InterviewDTO> getInterviewsByCandidate(Integer candidateId);

    List<InterviewDTO> getInterviewsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
}
