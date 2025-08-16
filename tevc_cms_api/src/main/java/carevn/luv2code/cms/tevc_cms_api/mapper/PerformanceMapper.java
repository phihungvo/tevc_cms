package carevn.luv2code.cms.tevc_cms_api.mapper;

import org.mapstruct.*;

import carevn.luv2code.cms.tevc_cms_api.dto.PerformanceDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Performance;

@Mapper(componentModel = "spring")
public interface PerformanceMapper {

    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(
            target = "employeeName",
            expression =
                    "java(performance.getEmployee() != null ? performance.getEmployee().getFirstName() + \" \" + performance.getEmployee().getLastName() : null)")
    @Mapping(target = "reviewerId", source = "reviewer.id")
    @Mapping(
            target = "reviewerName",
            expression =
                    "java(performance.getReviewer() != null ? performance.getReviewer().getFirstName() + \" \" + performance.getReviewer().getLastName() : null)")
    PerformanceDTO toDTO(Performance performance);

    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "reviewer", ignore = true)
    Performance toEntity(PerformanceDTO performanceDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "reviewer", ignore = true)
    void updateFromDto(PerformanceDTO dto, @MappingTarget Performance entity);
}
