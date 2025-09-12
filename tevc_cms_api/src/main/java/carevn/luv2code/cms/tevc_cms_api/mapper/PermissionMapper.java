package carevn.luv2code.cms.tevc_cms_api.mapper;

import org.mapstruct.*;

import carevn.luv2code.cms.tevc_cms_api.dto.PermissionDTO;
import carevn.luv2code.cms.tevc_cms_api.dto.RoleDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Permission;
import carevn.luv2code.cms.tevc_cms_api.entity.Role;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    //    @Mapping(target = "permissions", ignore = true)
    PermissionDTO toDTO(Permission entity);

    //    Role toEntity(RoleDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateProjectFromDto(RoleDTO dto, @MappingTarget Role entity);
}
