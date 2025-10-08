package carevn.luv2code.cms.tevc_cms_api.mapper;

import org.mapstruct.*;

import carevn.luv2code.cms.tevc_cms_api.dto.InterviewDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Interview;

@Mapper(componentModel = "spring")
public interface InterviewMapper {

    @Mapping(target = "interviewerId", source = "interviewer.id")
    @Mapping(target = "candidateId", source = "candidate.id")
    @Mapping(target = "jobPostingId", source = "jobPosting.id")
    @Mapping(target = "interviewStatus", source = "status")
    @Mapping(
            target = "interviewerName",
            expression =
                    "java(interview.getInterviewer() != null ? interview.getInterviewer().getLastName() + ' ' + interview.getInterviewer().getFirstName() : null)")
    @Mapping(
            target = "candidateName",
            expression =
                    "java(interview.getCandidate() != null ? interview.getCandidate().getFirstName() + ' ' + interview.getCandidate().getLastName() : null)")
    InterviewDTO toDTO(Interview interview);

    @Mapping(target = "interviewer", ignore = true)
    @Mapping(target = "candidate", ignore = true)
    @Mapping(target = "jobPosting", ignore = true)
    @Mapping(target = "status", ignore = true)
    Interview toEntity(InterviewDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "interviewer", ignore = true)
    @Mapping(target = "candidate", ignore = true)
    @Mapping(target = "jobPosting", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateInterviewFromDto(InterviewDTO dto, @MappingTarget Interview entity);
}
