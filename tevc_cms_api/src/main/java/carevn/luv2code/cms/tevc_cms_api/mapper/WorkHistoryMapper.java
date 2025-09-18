package carevn.luv2code.cms.tevc_cms_api.mapper;

import org.mapstruct.*;

import carevn.luv2code.cms.tevc_cms_api.dto.WorkHistoryDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.WorkHistory;

@Mapper(componentModel = "spring")
public interface WorkHistoryMapper {

    @Mapping(target = "employeeId", source = "employee.id")
    WorkHistoryDTO toDTO(WorkHistory workHistory);

    @Mapping(target = "employee", ignore = true)
    WorkHistory toEntity(WorkHistoryDTO workHistoryDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "employee", ignore = true)
    void updateEntityFromDTO(WorkHistoryDTO workHistoryDTO, @MappingTarget WorkHistory workHistory);
}
