package carevn.luv2code.cms.tevc_cms_api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import carevn.luv2code.cms.tevc_cms_api.dto.RoleDTO;

public interface RoleService {
    RoleDTO createRole(RoleDTO roleDTO);

    RoleDTO updateRole(UUID id, RoleDTO roleDTO);

    void deleteRole(UUID id);

    RoleDTO getRole(UUID id);

    Page<RoleDTO> getAllRoles(int page, int size);

    List<RoleDTO> getAllRolesNoPaging();

    void assignPermissionsToRole(UUID roleId, List<UUID> permissionIds);

    void removePermissionFromRole(UUID roleId, UUID permissionId);
}
