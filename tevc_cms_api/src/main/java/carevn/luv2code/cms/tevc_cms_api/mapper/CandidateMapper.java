package carevn.luv2code.cms.tevc_cms_api.mapper;

import org.mapstruct.*;

import carevn.luv2code.cms.tevc_cms_api.dto.CandidateDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Candidate;

@Mapper(componentModel = "spring")
public interface CandidateMapper {

    //    @Mapping(
    //            target = "jobPostingId",
    //            expression =
    //                    "java(candidate.getJobPostings().stream().map(job ->
    // job.getId().toString()).collect(Collectors.toList()))")
    @Mapping(target = "jobPostingId", ignore = true)
    CandidateDTO toDTO(Candidate candidate);

    @Mapping(target = "jobPostings", ignore = true) // JobPostings will be handled manually
    Candidate toEntity(CandidateDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCandidateFromDto(CandidateDTO dto, @MappingTarget Candidate entity);
}
