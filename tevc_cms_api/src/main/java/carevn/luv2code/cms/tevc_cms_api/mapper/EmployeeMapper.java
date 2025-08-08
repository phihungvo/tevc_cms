package carevn.luv2code.cms.tevc_cms_api.mapper;

import carevn.luv2code.cms.tevc_cms_api.dto.EmployeeDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Employee;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {DepartmentMapper.class, PositionMapper.class})
public interface EmployeeMapper {
    
    @Mapping(target = "departmentId", source = "department.id")
//    @Mapping(target = "departmentName", source = "department.name")
    @Mapping(target = "positionId", source = "position.id")
    EmployeeDTO toDTO(Employee employee);

    @Mapping(target = "department", ignore = true)
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "attendances", ignore = true)
    @Mapping(target = "leaves", ignore = true)
    @Mapping(target = "salaries", ignore = true)
    @Mapping(target = "performances", ignore = true)
    @Mapping(target = "managedDepartments", ignore = true)
    Employee toEntity(EmployeeDTO employeeDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "attendances", ignore = true)
    @Mapping(target = "leaves", ignore = true)
    @Mapping(target = "salaries", ignore = true)
    @Mapping(target = "performances", ignore = true)
    @Mapping(target = "managedDepartments", ignore = true)
    void updateEmployeeFromDto(EmployeeDTO dto, @MappingTarget Employee entity);
}
