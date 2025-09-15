package carevn.luv2code.cms.tevc_cms_api.mapper;

import org.mapstruct.*;

import carevn.luv2code.cms.tevc_cms_api.dto.TeamDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Team;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(
            target = "employeeIds",
            expression =
                    "java(team.getEmployees().stream().map(e -> e.getId()).collect(java.util.stream.Collectors.toSet()))")
    TeamDTO toDTO(Team team);

    @Mapping(target = "department", ignore = true)
    @Mapping(target = "employees", ignore = true)
    Team toEntity(TeamDTO teamDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "employees", ignore = true)
    void updateEntityFromDTO(TeamDTO teamDTO, @MappingTarget Team team);
}
