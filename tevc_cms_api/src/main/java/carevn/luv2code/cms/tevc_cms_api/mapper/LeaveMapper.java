package carevn.luv2code.cms.tevc_cms_api.mapper;

import org.mapstruct.*;

import carevn.luv2code.cms.tevc_cms_api.dto.LeaveDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Leave;

@Mapper(componentModel = "spring")
public interface LeaveMapper {

    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(
            target = "employeeName",
            expression =
                    "java(leave.getEmployee() != null ? leave.getEmployee().getFirstName() + \" \" + leave.getEmployee().getLastName() : null)")
    LeaveDTO toDTO(Leave leave);

    @Mapping(target = "employee", ignore = true)
    Leave toEntity(LeaveDTO leaveDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employee", ignore = true)
    void updateFromDto(LeaveDTO dto, @MappingTarget Leave entity);
}
