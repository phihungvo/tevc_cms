package carevn.luv2code.cms.tevc_cms_api.service;

import java.util.List;

import org.springframework.data.domain.Page;

import carevn.luv2code.cms.tevc_cms_api.dto.RoleDTO;

public interface RoleService {
    RoleDTO createRole(RoleDTO roleDTO);

    RoleDTO updateRole(Integer id, RoleDTO roleDTO);

    void deleteRole(Integer id);

    RoleDTO getRole(Integer id);

    Page<RoleDTO> getAllRoles(int page, int size);

    List<RoleDTO> getAllRolesNoPaging();

    void assignPermissionsToRole(Integer roleId, List<Integer> permissionIds);

    void removePermissionFromRole(Integer roleId, Integer permissionId);
}
