package carevn.luv2code.cms.tevc_cms_api.mapper;

import carevn.luv2code.cms.tevc_cms_api.dto.DepartmentDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Department;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    
    @Mapping(target = "managerId", source = "manager.id")
    @Mapping(target = "managerName", expression = "java(department.getManager() != null ? department.getManager().getFirstName() + \" \" + department.getManager().getLastName() : null)")
    @Mapping(target = "employeeCount", expression = "java(department.getEmployees() != null ? department.getEmployees().size() : 0)")
    DepartmentDTO toDTO(Department department);

    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "employees", ignore = true)
    Department toEntity(DepartmentDTO departmentDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "employees", ignore = true)
    void updateDepartmentFromDto(DepartmentDTO dto, @MappingTarget Department entity);
}
