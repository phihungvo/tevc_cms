package carevn.luv2code.cms.tevc_cms_api.mapper;

import org.mapstruct.*;

import carevn.luv2code.cms.tevc_cms_api.dto.EducationDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Education;

@Mapper(componentModel = "spring")
public interface EducationMapper {

    @Mapping(target = "employeeId", source = "employee.id")
    EducationDTO toDTO(Education education);

    @Mapping(target = "employee", ignore = true)
    Education toEntity(EducationDTO educationDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "employee", ignore = true)
    void updateEntityFromDTO(EducationDTO educationDTO, @MappingTarget Education education);
}
