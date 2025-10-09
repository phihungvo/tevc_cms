package carevn.luv2code.cms.tevc_cms_api.mapper;

import java.util.Collections;
import java.util.stream.Collectors;

import org.mapstruct.*;

import carevn.luv2code.cms.tevc_cms_api.dto.JobPostingDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.JobPosting;

@Mapper(componentModel = "spring")
public interface JobPostingMapper {

    @Mapping(source = "status", target = "jobPostingStatus")
    @Mapping(source = "department.id", target = "department")
    @Mapping(source = "position.id", target = "position")
    @Mapping(source = "recruiter.id", target = "recruiter")
    @Mapping(target = "applicantCount", ignore = true)
    @Mapping(target = "candidateIds", ignore = true)
    JobPostingDTO toDTO(JobPosting jobPosting);

    @AfterMapping
    default void afterToDto(JobPosting jobPosting, @MappingTarget JobPostingDTO dto) {
        if (jobPosting.getCandidates() != null) {
            dto.setApplicantCount(jobPosting.getCandidates().size());
            dto.setCandidateIds(
                    jobPosting.getCandidates().stream().map(c -> c.getId()).collect(Collectors.toList()));
        } else {
            dto.setApplicantCount(0);
            dto.setCandidateIds(Collections.emptyList());
        }
    }

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "recruiter", ignore = true)
    @Mapping(target = "candidates", ignore = true)
    JobPosting toEntity(JobPostingDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "recruiter", ignore = true)
    @Mapping(target = "candidates", ignore = true)
    void updateEntityFromDto(JobPostingDTO dto, @MappingTarget JobPosting entity);
}
