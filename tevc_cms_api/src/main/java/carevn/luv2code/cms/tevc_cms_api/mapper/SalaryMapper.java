package carevn.luv2code.cms.tevc_cms_api.mapper;

import org.mapstruct.*;

import carevn.luv2code.cms.tevc_cms_api.dto.SalaryDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Employee;
import carevn.luv2code.cms.tevc_cms_api.entity.Salary;

@Mapper(componentModel = "spring")
public interface SalaryMapper {

    @Mapping(target = "employeeId", source = "employee.id")
    //    @Mapping(target = "employeeName", source = "employee.name")
    SalaryDTO toDTO(Salary salary);

    // @Mapping(target = "employee", expression = "java(mapEmployee(dto.getEmployeeId()))")
    Salary toEntity(SalaryDTO salaryDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employee", expression = "java(mapEmployee(dto.getEmployeeId()))")
    void updateSalaryFromDto(SalaryDTO dto, @MappingTarget Salary entity);

    default Employee mapEmployee(Integer employeeId) {
        if (employeeId == null) {
            return null;
        }
        Employee employee = new Employee();
        //        employee.setId(employeeId);
        return employee;
    }
}
