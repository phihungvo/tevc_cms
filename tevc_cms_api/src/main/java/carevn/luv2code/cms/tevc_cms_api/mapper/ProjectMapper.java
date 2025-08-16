package carevn.luv2code.cms.tevc_cms_api.mapper;

import org.mapstruct.*;

import carevn.luv2code.cms.tevc_cms_api.dto.ProjectDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Project;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    @Mapping(target = "projectManagerId", source = "projectManager.id")
    @Mapping(
            target = "projectManagerName",
            expression =
                    "java(project.getProjectManager() != null ? project.getProjectManager().getFirstName() + \" \" + project.getProjectManager().getLastName() : null)")
    @Mapping(
            target = "memberIds",
            expression =
                    "java(project.getMembers().stream().map(Employee::getId).collect(java.util.stream.Collectors.toList()))")
    @Mapping(target = "memberCount", expression = "java(project.getMembers().size())")
    ProjectDTO toDTO(Project project);

    @Mapping(target = "projectManager", ignore = true)
    @Mapping(target = "members", ignore = true)
    Project toEntity(ProjectDTO projectDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "projectManager", ignore = true)
    @Mapping(target = "members", ignore = true)
    void updateProjectFromDto(ProjectDTO dto, @MappingTarget Project entity);
}
