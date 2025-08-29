package carevn.luv2code.cms.tevc_cms_api.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import carevn.luv2code.cms.tevc_cms_api.dto.InterviewDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Interview;

@Mapper(componentModel = "spring")
public interface InterviewMapper {

    InterviewDTO toDTO(Interview interview);

    Interview toEntity(InterviewDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateInterviewFromDto(InterviewDTO dto, @MappingTarget Interview entity);
}
