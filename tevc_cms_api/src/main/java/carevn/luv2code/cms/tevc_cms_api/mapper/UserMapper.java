package carevn.luv2code.cms.tevc_cms_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import carevn.luv2code.cms.tevc_cms_api.dto.UserDTO;
import carevn.luv2code.cms.tevc_cms_api.dto.requests.UserUpdateRequest;
import carevn.luv2code.cms.tevc_cms_api.entity.User;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", ignore = true)
    //        @Mapping(target = "permissions", ignore = true)
    UserDTO toDTO(User user);

    @Mapping(target = "roles", ignore = true)
    //        @Mapping(target = "permissions", ignore = true)
    User toEntity(UserDTO userDTO);

    @Mapping(target = "roles", ignore = true)
    //    @Mapping(target = "permissions", ignore = true)
    void updateUserFromDto(UserUpdateRequest request, @MappingTarget User user);
}
