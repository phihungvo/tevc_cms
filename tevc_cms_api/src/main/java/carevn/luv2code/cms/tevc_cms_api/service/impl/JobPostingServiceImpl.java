package carevn.luv2code.cms.tevc_cms_api.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import carevn.luv2code.cms.tevc_cms_api.dto.JobPostingDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.*;
import carevn.luv2code.cms.tevc_cms_api.enums.JobPostingStatus;
import carevn.luv2code.cms.tevc_cms_api.exception.AppException;
import carevn.luv2code.cms.tevc_cms_api.exception.ErrorCode;
import carevn.luv2code.cms.tevc_cms_api.mapper.JobPostingMapper;
import carevn.luv2code.cms.tevc_cms_api.repository.*;
import carevn.luv2code.cms.tevc_cms_api.service.JobPostingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobPostingServiceImpl implements JobPostingService {

    private final CandidateRepository candidateRepository;
    private final JobPostingRepository jobPostingRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final EmployeeRepository employeeRepository;
    private final JobPostingMapper jobPostingMapper;

    @Override
    public Page<JobPostingDTO> getJobPostingPaginated(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return jobPostingRepository.findAll(pageRequest).map(jobPostingMapper::toDTO);
    }

    @Override
    public JobPostingDTO create(JobPostingDTO dto) {
        JobPosting entity = jobPostingMapper.toEntity(dto);

        if (dto.getJobPostingStatus() != null) {
            entity.setStatus(JobPostingStatus.valueOf(dto.getJobPostingStatus()));
        }
        if (dto.getDepartment() != null) {
            entity.setDepartment(departmentRepository
                    .findById(dto.getDepartment())
                    .orElseThrow(() -> new EntityNotFoundException("Department not found")));
        }
        if (dto.getPosition() != null) {
            entity.setPosition(positionRepository
                    .findById(dto.getPosition())
                    .orElseThrow(() -> new EntityNotFoundException("Position not found")));
        }
        if (dto.getRecruiter() != null) {
            entity.setRecruiter(employeeRepository
                    .findById(dto.getRecruiter())
                    .orElseThrow(() -> new EntityNotFoundException("Recruiter not found")));
        }

        return jobPostingMapper.toDTO(jobPostingRepository.save(entity));
    }

    @Override
    public JobPostingDTO update(Integer id, JobPostingDTO dto) {
        JobPosting entity = jobPostingRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("JobPosting not found"));

        jobPostingMapper.updateEntityFromDto(dto, entity);

        if (dto.getJobPostingStatus() != null) {
            entity.setStatus(JobPostingStatus.valueOf(dto.getJobPostingStatus()));
        }
        if (dto.getDepartment() != null) {
            entity.setDepartment(departmentRepository
                    .findById(dto.getDepartment())
                    .orElseThrow(() -> new EntityNotFoundException("Department not found")));
        }
        if (dto.getPosition() != null) {
            entity.setPosition(positionRepository
                    .findById(dto.getPosition())
                    .orElseThrow(() -> new EntityNotFoundException("Position not found")));
        }
        if (dto.getRecruiter() != null) {
            entity.setRecruiter(employeeRepository
                    .findById(dto.getRecruiter())
                    .orElseThrow(() -> new EntityNotFoundException("Recruiter not found")));
        }

        return jobPostingMapper.toDTO(jobPostingRepository.save(entity));
    }

    @Override
    public void delete(Integer id) {
        if (!jobPostingRepository.existsById(id)) {
            throw new EntityNotFoundException("JobPosting not found");
        }
        jobPostingRepository.deleteById(id);
    }

    @Override
    public JobPostingDTO getById(Integer id) {
        JobPosting entity = jobPostingRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("JobPosting not found"));
        return jobPostingMapper.toDTO(entity);
    }

    @Override
    public List<JobPostingDTO> getJobPostingsByCandidate(Integer candidateId) {
        Candidate candidate = candidateRepository
                .findById(candidateId)
                .orElseThrow(() -> new AppException(ErrorCode.CANDIDATE_NOT_FOUND));
        return candidate.getJobPostings().stream().map(jobPostingMapper::toDTO).collect(Collectors.toList());
    }
}
