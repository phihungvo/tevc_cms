package carevn.luv2code.cms.tevc_cms_api.mapper;

import org.mapstruct.*;

import carevn.luv2code.cms.tevc_cms_api.dto.PayrollDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Payroll;

@Mapper(componentModel = "spring")
public interface PayrollMapper {

    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(
            target = "employeeName",
            expression =
                    "java(payroll.getEmployee() != null ? payroll.getEmployee().getFirstName() + \" \" + payroll.getEmployee().getLastName() : null)")
    PayrollDTO toDTO(Payroll payroll);

    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "details", ignore = true)
    Payroll toEntity(PayrollDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateFromDto(PayrollDTO dto, @MappingTarget Payroll entity);
}
