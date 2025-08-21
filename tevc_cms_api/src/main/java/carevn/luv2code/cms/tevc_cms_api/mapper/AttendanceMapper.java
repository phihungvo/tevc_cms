package carevn.luv2code.cms.tevc_cms_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import carevn.luv2code.cms.tevc_cms_api.dto.AttendanceDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Attendance;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {
    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(
            target = "employeeName",
            expression =
                    "java(attendance.getEmployee().getFirstName() + \" \" + attendance.getEmployee().getLastName())")
    AttendanceDTO toDTO(Attendance attendance);

    @Mapping(target = "employee", ignore = true)
    Attendance toEntity(AttendanceDTO attendanceDTO);

    @Mapping(target = "employee", ignore = true)
    void updateEntityFromDTO(AttendanceDTO attendanceDTO, @MappingTarget Attendance attendance);
}
