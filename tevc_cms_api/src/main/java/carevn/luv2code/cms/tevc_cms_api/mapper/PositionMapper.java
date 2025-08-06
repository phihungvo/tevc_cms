package carevn.luv2code.cms.tevc_cms_api.mapper;

import carevn.luv2code.cms.tevc_cms_api.dto.PositionDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Position;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PositionMapper {
    default PositionDTO toDTO(Position position) {
        try {
            PositionDTO dto = new PositionDTO();
            dto.setId(position.getId());
            dto.setTitle(position.getTitle());
            dto.setDescription(position.getDescription());
            dto.setBaseSalary(position.getBaseSalary());
            dto.setPositionType(position.getPositionType() != null ? position.getPositionType().name() : null);
            dto.setEmployeeCount(position.getEmployees() != null ? position.getEmployees().size() : 0);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Mapping error", e);
        }
    }

//    @Mapping(target = "employeeCount", expression = "java(position.getEmployees() != null ? position.getEmployees().size() : 0)")
//    PositionDTO toDTO(Position position);

    @Mapping(target = "employees", ignore = true)
    Position toEntity(PositionDTO positionDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employees", ignore = true)
    void updatePositionFromDto(PositionDTO dto, @MappingTarget Position entity);
}