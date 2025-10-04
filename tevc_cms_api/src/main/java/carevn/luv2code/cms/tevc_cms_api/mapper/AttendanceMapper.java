package carevn.luv2code.cms.tevc_cms_api.mapper;

import org.mapstruct.*;

import carevn.luv2code.cms.tevc_cms_api.dto.AttendanceDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Attendance;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {
    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(
            target = "employeeName",
            expression =
                    "java(attendance.getEmployee().getLastName() + \" \" + attendance.getEmployee().getFirstName())")
    @Mapping(target = "employeeCode", source = "employee.employeeCode")
    AttendanceDTO toDTO(Attendance attendance);

    @Mapping(target = "employee", ignore = true)
    Attendance toEntity(AttendanceDTO attendanceDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "employee", ignore = true)
    void updateEntityFromDTO(AttendanceDTO attendanceDTO, @MappingTarget Attendance attendance);
}
