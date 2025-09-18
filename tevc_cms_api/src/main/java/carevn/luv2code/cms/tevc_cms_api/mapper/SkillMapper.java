package carevn.luv2code.cms.tevc_cms_api.mapper;

import org.mapstruct.*;

import carevn.luv2code.cms.tevc_cms_api.dto.SkillDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Skill;

@Mapper(componentModel = "spring")
public interface SkillMapper {

    @Mapping(
            target = "employeesIds",
            expression =
                    "java(skill.getEmployees().stream().map(e -> e.getId()).collect(java.util.stream.Collectors.toList()))")
    SkillDTO toDTO(Skill skill);

    @Mapping(target = "employees", ignore = true)
    Skill toEntity(SkillDTO skillDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "employees", ignore = true)
    void updateEntityFromDTO(SkillDTO skillDTO, @MappingTarget Skill skill);
}
