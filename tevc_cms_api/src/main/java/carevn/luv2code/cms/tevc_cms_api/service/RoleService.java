package carevn.luv2code.cms.tevc_cms_api.service;

import carevn.luv2code.cms.tevc_cms_api.dto.RoleDTO;
import carevn.luv2code.cms.tevc_cms_api.dto.requests.AssignPermissionRequest;
import carevn.luv2code.cms.tevc_cms_api.dto.requests.CreateRoleRequest;
import carevn.luv2code.cms.tevc_cms_api.dto.requests.UpdateRoleRequest;

public interface RoleService {
    //    RoleDTO createRole(RoleDTO roleDTO);

    RoleDTO createRole(CreateRoleRequest request);

    RoleDTO assignPermissions(AssignPermissionRequest request);

    RoleDTO updateRole(Integer id, UpdateRoleRequest request);

    //    RoleDTO updateRole(UUID id, RoleDTO roleDTO);
    //
    //    void deleteRole(UUID id);
    //
    //    RoleDTO getRole(UUID id);
    //
    //    Page<RoleDTO> getAllRoles(int page, int size);
    //
    //    List<RoleDTO> getAllRolesNoPaging();
    //
    //    void assignPermissionsToRole(UUID roleId, List<UUID> permissionIds);
    //
    //    void removePermissionFromRole(UUID roleId, UUID permissionId);
}
