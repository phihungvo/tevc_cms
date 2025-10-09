package carevn.luv2code.cms.tevc_cms_api.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import carevn.luv2code.cms.tevc_cms_api.dto.InterviewDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Candidate;
import carevn.luv2code.cms.tevc_cms_api.entity.Employee;
import carevn.luv2code.cms.tevc_cms_api.entity.Interview;
import carevn.luv2code.cms.tevc_cms_api.entity.JobPosting;
import carevn.luv2code.cms.tevc_cms_api.enums.InterviewStatus;
import carevn.luv2code.cms.tevc_cms_api.exception.AppException;
import carevn.luv2code.cms.tevc_cms_api.exception.ErrorCode;
import carevn.luv2code.cms.tevc_cms_api.mapper.InterviewMapper;
import carevn.luv2code.cms.tevc_cms_api.repository.CandidateRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.EmployeeRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.InterviewRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.JobPostingRepository;
import carevn.luv2code.cms.tevc_cms_api.service.InterviewService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {

    private final InterviewRepository interviewRepository;
    private final EmployeeRepository employeeRepository;
    private final JobPostingRepository jobPostingRepository;
    private final CandidateRepository candidateRepository; // Added CandidateRepository
    private final InterviewMapper interviewMapper;

    @Override
    public void createInterview(InterviewDTO interviewDTO) {
        JobPosting jobPosting = jobPostingRepository
                .findById(interviewDTO.getJobPostingId())
                .orElseThrow(() -> new AppException(ErrorCode.JOB_POSTING_NOT_FOUND));

        Candidate candidate = candidateRepository
                .findById(interviewDTO.getCandidateId())
                .orElseThrow(() -> new AppException(ErrorCode.CANDIDATE_NOT_FOUND));

        Employee interviewer = employeeRepository
                .findById(interviewDTO.getInterviewerId())
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

        Interview interview = Interview.builder()
                .candidate(candidate)
                .jobPosting(jobPosting)
                .interviewDate(java.sql.Timestamp.valueOf(String.valueOf(interviewDTO.getInterviewDate())))
                .interviewer(interviewer)
                .status(InterviewStatus.SCHEDULED)
                .feedback(interviewDTO.getFeedback())
                .rating(interviewDTO.getRating())
                .build();

        interviewRepository.save(interview);
    }

    @Override
    public void updateInterview(Integer interviewId, LocalDateTime newDate, String newInterviewer) {
        //        Interview interview = interviewRepository
        //                .findById(interviewId)
        //                .orElseThrow(() -> new IllegalArgumentException("Interview not found"));
        //        interview.setInterviewDate(java.sql.Timestamp.valueOf(newDate));
        //        interview.setInterviewer(newInterviewer);
        //        interviewRepository.save(interview);
    }

    @Override
    public void cancelInterview(Integer interviewId) {
        Interview interview = interviewRepository
                .findById(interviewId)
                .orElseThrow(() -> new IllegalArgumentException("Interview not found"));
        interview.setStatus(InterviewStatus.CANCELED);
        interviewRepository.save(interview);
    }

    @Override
    public InterviewDTO getInterview(Integer interviewId) {
        Interview interview = interviewRepository
                .findById(interviewId)
                .orElseThrow(() -> new IllegalArgumentException("Interview not found"));
        return interviewMapper.toDTO(interview);
    }

    @Override
    public List<InterviewDTO> getAllInterviews() {
        return interviewRepository.findAll().stream()
                .map(interviewMapper::toDTO)
                .toList();
    }

    @Override
    public Page<InterviewDTO> getInterviewsPaginated(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return interviewRepository.findAll(pageRequest).map(interviewMapper::toDTO);
    }

    //    @Override
    //    public List<InterviewDTO> getInterviewsByCandidate(Integer candidateId) {
    //        return interviewRepository.findByCandidateId(candidateId).stream()
    //                .map(interviewMapper::toDTO)
    //                .toList();
    //    }

    @Override
    public Page<InterviewDTO> getInterviewsByCandidate(Integer candidateId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("interviewDate").descending());
        return interviewRepository.findByCandidateId(candidateId, pageable).map(interviewMapper::toDTO);
    }

    @Override
    public Page<InterviewDTO> getInterviewsByJobPosting(Integer jobPostingId, int page, int size) {
        List<Integer> candidateIds = candidateRepository.findIdsByJobPostingId(jobPostingId);

        if (candidateIds.isEmpty()) {
            return Page.empty();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Interview> interviewPage = interviewRepository.findByCandidateIdIn(candidateIds, pageable);

        return interviewPage.map(interviewMapper::toDTO);
    }

    @Override
    public Page<InterviewDTO> getInterviewsByCandidates(List<Integer> candidateIds, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Interview> interviews = interviewRepository.findByCandidateIdIn(candidateIds, pageable);
        return interviews.map(interviewMapper::toDTO);
    }

    @Override
    public List<InterviewDTO> getInterviewsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return interviewRepository
                .findByInterviewDateBetween(java.sql.Timestamp.valueOf(startDate), java.sql.Timestamp.valueOf(endDate))
                .stream()
                .map(interviewMapper::toDTO)
                .toList();
    }
}
