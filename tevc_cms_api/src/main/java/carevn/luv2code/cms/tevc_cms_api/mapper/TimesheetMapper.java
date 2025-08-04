package carevn.luv2code.cms.tevc_cms_api.mapper;

import carevn.luv2code.cms.tevc_cms_api.dto.TimesheetDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Timesheet;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TimesheetMapper {
    
    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "employeeName", expression = "java(timesheet.getEmployee() != null ? timesheet.getEmployee().getFirstName() + \" \" + timesheet.getEmployee().getLastName() : null)")
    @Mapping(target = "projectId", source = "project.id")
    @Mapping(target = "projectName", source = "project.name")
    @Mapping(target = "approverId", source = "approver.id")
    @Mapping(target = "approverName", expression = "java(timesheet.getApprover() != null ? timesheet.getApprover().getFirstName() + \" \" + timesheet.getApprover().getLastName() : null)")
    TimesheetDTO toDTO(Timesheet timesheet);

    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "approver", ignore = true)
    Timesheet toEntity(TimesheetDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateFromDto(TimesheetDTO dto, @MappingTarget Timesheet entity);
}
