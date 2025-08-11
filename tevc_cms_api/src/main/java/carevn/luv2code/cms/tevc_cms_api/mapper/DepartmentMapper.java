package carevn.luv2code.cms.tevc_cms_api.mapper;

import carevn.luv2code.cms.tevc_cms_api.dto.DepartmentDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Department;
import carevn.luv2code.cms.tevc_cms_api.entity.Employee;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    @Mapping(target = "managerId", source = "manager.id")
//    @Mapping(target = "managerName", expression = "java(getManagerName(department.getManager()))")
    @Mapping(target = "employeeCount", expression = "java(getEmployeeCount(department))")
    DepartmentDTO toDTO(Department department);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "employees", ignore = true)
    Department toEntity(DepartmentDTO departmentDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "employees", ignore = true)
    void updateDepartmentFromDto(DepartmentDTO dto, @MappingTarget Department entity);

    default String getManagerName(Employee manager) {
        if (manager == null) return null;
        try {
            String firstName = manager.getFirstName() != null ? manager.getFirstName() : "";
            String lastName = manager.getLastName() != null ? manager.getLastName() : "";
            return (firstName + " " + lastName).trim();
        } catch (Exception e) {
            return "N/A";
        }
    }

    default int getEmployeeCount(Department department) {
        try {
            return department.getEmployees() != null ? department.getEmployees().size() : 0;
        } catch (Exception e) {
            // Nếu có lazy loading exception, return 0
            return 0;
        }
    }
}