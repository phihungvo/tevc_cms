package carevn.luv2code.cms.tevc_cms_api.mapper;

import carevn.luv2code.cms.tevc_cms_api.dto.PositionDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Position;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PositionMapper {

//    @Mapping(target = "employeeCount", expression = "java(position.getEmployees() != null ? position.getEmployees().size() : 0)")
    PositionDTO toDTO(Position position);

//    @Mapping(target = "employees", ignore = true)
//    @Mapping(target = "positionType", expression = "java(dto.getPositionType() != null ? carevn.luv2code.cms.tevc_cms_api.enums.PositionType.valueOf(dto.getPositionType()) : null)")
    Position toEntity(PositionDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    //@Mapping(target = "employees", ignore = true)
    @Mapping(target = "positionType", expression = "java(dto.getPositionType() != null ? carevn.luv2code.cms.tevc_cms_api.enums.PositionType.valueOf(dto.getPositionType()) : null)")
    void updatePositionFromDto(PositionDTO dto, @MappingTarget Position entity);
}